package shimmer.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import shimmer.domain.Bug;
import shimmer.domain.Graph;
import shimmer.service.FindbugsService;
import edu.umd.cs.findbugs.FindBugs2;

/**
 * Standard implementation of methods to operate with Findbugs Library.
 * 
 * @author Filip Daca
 */
@Named
public class FindbugsServiceImpl implements FindbugsService {
	
	// ************************************************************************
	// STATIC FIELDS
	
	private static final String TEMPORARY_ANALYSIS_FILE = "findbugs-report.xml";
	
	// ************************************************************************
	// TOOLS
	
	@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject
	@Named("fileProperties")
	private Properties fileProperties;

	// ************************************************************************
	// IMPLEMENTATIONS
	
	@Override
	public void applyAnalysis(Graph graph, String directoryPath, boolean highPriority) {
		String analysisFilePath = fileProperties.getProperty("fileSystem.temp") + TEMPORARY_ANALYSIS_FILE;
		runAnalysis(analysisFilePath, directoryPath, highPriority);
		
		File analysisFile = new File(analysisFilePath);
		if (analysisFile.exists()) {
			try {
				parseAnalysisResults(analysisFile, graph);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			} finally {
				analysisFile.delete();
			}
		}
	}

	// ************************************************************************
	// HELPER METHORS
	
	/**
	 * Parse analysis result and applies bug information to graph. 
	 * @param analysisFile - file with findbugs output
	 * @param graph - shimmer graph instance
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private void parseAnalysisResults(File analysisFile, Graph graph) 
			throws ParserConfigurationException, SAXException, IOException {
		//Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		 
		//Build Document
		Document document = builder.parse(analysisFile);
		 
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
		 
		Element root = document.getDocumentElement();
		NodeList bugElements = root.getElementsByTagName("BugInstance");
		for (int i = 0; i < bugElements.getLength(); i++) {
			Element bugElement = (Element) bugElements.item(i);
			String bugType = bugElement.getAttribute("type");
			String bugCategory = bugElement.getAttribute("category");
			String bugAbbrev = bugElement.getAttribute("abbrev");
			int bugRank = Integer.parseInt(bugElement.getAttribute("rank"));
			int bugPriority = Integer.parseInt(bugElement.getAttribute("priority"));
			
			Element classElement = (Element) bugElement.getElementsByTagName("Class").item(0);
			String className = classElement.getAttribute("classname");
			
			Bug bug = new Bug(bugType, bugCategory, bugAbbrev, bugRank, bugPriority, className);
			
			// Adding bug element to ShimmerGraph
			graph.addBug(bug, className);
		}
		
		NodeList packageElements = root.getElementsByTagName("PackageStats");
		for (int i = 0; i < packageElements.getLength(); i++) {
			Element packageElement = (Element) packageElements.item(i);
			String packageName = packageElement.getAttribute("package");
			int totalBugs = Integer.parseInt(packageElement.getAttribute("total_bugs"));
			int totalSize = Integer.parseInt(packageElement.getAttribute("total_size"));
			
			int[] priorityBugs = new int[6];
			Arrays.fill(priorityBugs, 0);
			for (int j = 1; j < priorityBugs.length; j++) {
				if (packageElement.hasAttribute("priority_" + j)) {
					priorityBugs[j] = Integer.parseInt(packageElement.getAttribute("priority_" + j));
				}
			}
			
			NodeList classElements = packageElement.getElementsByTagName("ClassStats");
			int largestClassSize = 0;
			for (int j = 0; j < classElements.getLength(); j++) {
				Element classElement = (Element) classElements.item(j);
				int classSize = Integer.parseInt(classElement.getAttribute("size"));
				largestClassSize = Math.max(largestClassSize, classSize);
			}
			
			// Adding package analysis to graph
			graph.applyFindbugsPackageAnalysis(packageName, totalBugs, totalSize, largestClassSize, priorityBugs);
		}
	}

	/**
	 * Runs findbugs analysis.
	 * @param analysisFilePath - output file
	 * @param projectPath - input files
	 */
	private void runAnalysis(String analysisFilePath, String projectPath, boolean highPriority) {
		StringBuilder command = new StringBuilder();
		if (highPriority) {
			command.append("-effort:max ");
		} else {
			command.append("-effort:min -relaxed ");
		}
		command.append("-nested:false -xml ");
		command.append("-output ");
		command.append(analysisFilePath);
		command.append(" ");
		command.append(projectPath);
		
		try {
			FindBugs2.main(command.toString().split("\\s+"));
		} catch (Exception e) {
			System.err.println("Findbugs found some errors: " + e.getMessage());
		}
	}

}
