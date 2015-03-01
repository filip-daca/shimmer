package shimmer.domain;

import java.util.Collection;

import shimmer.domain.collections.Edges;
import shimmer.domain.collections.Nodes;
import shimmer.domain.factory.EdgeFactory;

/**
 * Class representing a Shimmer graph.
 * 
 * @author Filip Daca
 */
public class Graph {

	// ************************************************************************
	// FIELDS
	
	// Package structure - only used when package tree is needed
	private PackageTreeNode packageRoot;
	
	// Map with nodes (id -> node)
	private Nodes nodes;
	
	// Set with edges
	private Edges edges;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Graph() {
		this.packageRoot = new PackageTreeNode(null);
		this.nodes = new Nodes();
		this.edges = new Edges();
	}
	
	// ************************************************************************
	// METHODS

	/**
	 * Adds a node treated as package.
	 * 
	 * @param analysedPackageNode - package node to add
	 * @param buildPackageTreeEdges - build package tree edges?
	 * @param buildFullPackageTree - build package tree nodes?
	 */
	public void addAnalysedPackageNode(Node analysedPackageNode, 
			boolean buildPackageTreeEdges, 
			boolean buildFullPackageTree) {
		
		// Adding new node to data collections
		nodes.add(analysedPackageNode);
		
		if (buildPackageTreeEdges || buildFullPackageTree) {
			packageRoot.addJoint(nodes, analysedPackageNode, 
					analysedPackageNode.getName(), buildFullPackageTree);
		}
	}
	
	/**
	 * Generates edges of a package tree.
	 */
	public void generateTreeEdges() {
		for (PackageTreeNode packageTreeNode : packageRoot.getChildren().values()) {
			packageTreeNode.generateEdges(edges);
		}
	}
	
	/**
	 * Adds an efferent edge
	 * @param package1Name - name of package 1
	 * @param package2Name - name of package 2
	 * @param includeCount - number of iclusions
	 */
	public void addEfferentEdge(String package1Name, String package2Name, Integer includeCount) {
		Node packageNode1 = nodes.get(package1Name);
		Node packageNode2 = nodes.get(package2Name);
		edges.add(EdgeFactory.newDependencyEdge(packageNode1, packageNode2, includeCount));
	}

	// ************************************************************************
	// PRIVATE METHODS
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	/**
	 * @return number of nodes
	 */
	public int getNodesCount() {
		return nodes.count();
	}
	
	/**
	 * @return collection od edges
	 */
	public Collection<Edge> getEdges() {
		return edges.getCollection();
	}

	/**
	 * @return collection of nodes
	 */
	public Collection<Node> getNodes() {
		return nodes.getCollection();
	}
	
}
