package shimmer.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Named;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageComparator;
import shimmer.domain.Graph;
import shimmer.domain.Node;
import shimmer.service.JDependService;

/**
 * Standard implementation of methods to operate with JDepend.
 * 
 * @author filip.daca@javatech.com.pl
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
			// TODO: add a faces message
		}
		
		// Running JDepend analyzer
		Collection<JavaPackage> packages = analyzer.analyze();
        ArrayList<JavaPackage> packageList = new ArrayList<JavaPackage>(packages);
        Collections.sort(packageList, new PackageComparator(PackageComparator
                .byName()));
        
        // Generating graph
        Graph graph = new Graph();
        
        for (JavaPackage javaPackage : packageList) {
        	Node newPackageNode = new Node(graph.getNodesCount(), javaPackage.getName(), 
					javaPackage.getClassCount(), javaPackage.getVolatility(), 
					javaPackage.getAbstractClassCount(), javaPackage.getConcreteClassCount(),
					javaPackage.getEfferents().size(), javaPackage.getAfferents().size());
        	graph.addPackageNode(newPackageNode);
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
