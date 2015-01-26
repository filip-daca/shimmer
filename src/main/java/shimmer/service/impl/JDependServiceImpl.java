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
import shimmer.domain.factory.NodeFactory;
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
	public Graph generateGraph(String directoryName, 
			boolean buildPackageTreeEdges, boolean buildDependenciesEdges,
			boolean buildFullPackageTree, boolean buildLibraryPackages) {
		
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
        
        // Adding packages
        for (JavaPackage javaPackage : packageList) {
        	
        	// Skip library packages
        	if (javaPackage.isLibraryPackage() && !buildLibraryPackages){
        		continue;
        	}
        	
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
        	
        	graph.addAnalysedPackageNode(newNode, buildPackageTreeEdges, buildFullPackageTree);
        }
        
        // Adding package tree edges
        if (buildPackageTreeEdges) {
        	graph.generateTreeEdges();
        }
        
        // Adding dependencies edges
        if (buildDependenciesEdges) {
        	for (JavaPackage javaPackage : packageList) {
        		// Skip library packages
            	if (javaPackage.isLibraryPackage() && !buildLibraryPackages){
            		continue;
            	}
            	
            	for (JavaPackage efferentPackage : javaPackage.getEfferents()) {
            		// Skip library packages
            		if (efferentPackage.isLibraryPackage() && !buildLibraryPackages){
                		continue;
                	}

            		graph.addEfferentEdge(javaPackage.getName(), efferentPackage.getName(), 
            				javaPackage.getEfferentsCount().get(efferentPackage));
            	}
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
