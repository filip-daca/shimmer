package shimmer.domain;

import java.util.Collection;

import shimmer.domain.collections.Edges;
import shimmer.domain.collections.Nodes;

/**
 * Class representing a Shimmer graph.
 * 
 * @author filip.daca@javatech.com.pl
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
	 * @param newPackageNode - package node to add
	 * @param addTreeEdges - build package tree?
	 */
	public void addAnalysedPackageNode(Node newPackageNode, boolean addTreeEdges) {
		// Adding new node to data collections
		nodes.add(newPackageNode);
		if (addTreeEdges) {
			packageRoot.addNode(nodes, newPackageNode, newPackageNode.getName());
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
