package shimmer.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Named;

import org.omnifaces.util.Messages;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageComparator;
import shimmer.domain.Graph;
import shimmer.domain.Node;
import shimmer.domain.factory.NodeFactory;
import shimmer.service.JDependService;

/**
 * Standard implementation of methods to operate with JDepend.
 * 
 * @author Filip Daca
 */
@Named
public class JDependServiceImpl implements JDependService {
	
	// ************************************************************************
	// SERVICE FIELDS
	
	private JDepend analyzer;
	
	// ************************************************************************
	// IMPLEMENTATIONS
	
	@Override
	public Graph generateGraph(String directoryName) {
		
		analyzer = new JDepend();
		try {
			addDirectory(directoryName);
		} catch (IOException e) {
			Messages.addGlobalError("Unable to add directory to analyze");
		}
		
		// Running JDepend analyzer
		Collection<JavaPackage> packages = analyzer.analyze();
        ArrayList<JavaPackage> packageList = new ArrayList<JavaPackage>(packages);
        Collections.sort(packageList, new PackageComparator(PackageComparator
                .byName()));
        
        // Generating graph
        Graph graph = new Graph();
        
        // Adding packages
        for (JavaPackage javaPackage : packageList) {
        	
        	Node newNode;
        	if (javaPackage.isLibraryPackage()) {
        		newNode = NodeFactory.newLibraryPackageNode(javaPackage.getName(),
    				javaPackage.getEfferents().size(), javaPackage.getAfferents().size());
        	} else {
        		newNode = NodeFactory.newAnalysedPackageNode(javaPackage.getName(), 
    				javaPackage.getClassCount(), javaPackage.getAbstractClassCount(), 
    				javaPackage.getConcreteClassCount(),
    				javaPackage.getEfferents().size(), javaPackage.getAfferents().size());
        	}
        	
        	graph.addAnalysedPackageNode(newNode);
        }
        
        // Adding package tree edges
    	graph.generateTreeEdges(graph.getNodes());
        
        // Adding dependencies edges
    	for (JavaPackage javaPackage : packageList) {
        	for (JavaPackage efferentPackage : javaPackage.getEfferents()) {
        		graph.addEfferentEdge(javaPackage.getName(), efferentPackage.getName(), 
        				javaPackage.getEfferentsCount().get(efferentPackage));
        	}
    	}
		
        return graph;
	}
	
	// ************************************************************************
	// PRIVATE METHODS

	/**
     * Adds the specified directory name to the collection of directories to be
     * analyzed.
     * 
     * @param name Directory name.
     * @throws IOException If the directory does not exist.
     */
    private void addDirectory(String name) throws IOException {
        analyzer.addDirectory(name);
    }
	
}
