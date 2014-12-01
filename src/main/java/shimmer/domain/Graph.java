package shimmer.domain;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class representing a Shimmer graph.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class Graph {

	// ************************************************************************
	// FIELDS
	
	// Package structure
	private PackageTreeNode packageRoot;
	
	// Map with nodes (id -> node)
	private Map<Integer, Node> nodes;
	
	// Set with edges
	private Set<Edge> edges;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Graph() { 
		this.packageRoot = new PackageTreeNode(null);
		this.nodes = new LinkedHashMap<Integer, Node>();
		this.edges = new LinkedHashSet<Edge>();
	}
	
	// ************************************************************************
	// METHODS
	
	/**
	 * Adds a node with a tree manner.
	 * 
	 * @param parentId
	 * @deprecated
	 */
	public void addNode(int parentId) {
		int newId = nodes.size();
		Node newNode = new Node(newId);
		Node parentNode = nodes.get(parentId);
		
		Edge newEdge = new Edge(newNode, parentNode);
		newNode.getEdges().add(newEdge);
		parentNode.getEdges().add(newEdge);
		
		edges.add(newEdge);
		nodes.put(newId, newNode);
	}

	/**
	 * Adds a node treated as package.
	 * 
	 * @param newNode - package node
	 */
	public void addPackageNode(Node newNode) {
		Node parentNode = packageRoot.findParent(newNode.getName());
		
		if (parentNode != null) {
			Edge newEdge = new Edge(newNode, parentNode);
			newNode.getEdges().add(newEdge);
			parentNode.getEdges().add(newEdge);
			edges.add(newEdge);
		}
		
		nodes.put(newNode.getId(), newNode);
		packageRoot.addNode(newNode, newNode.getName());
	}
	
	/**
	 * Returns number of nodes.
	 * 
	 * @return number of nodes
	 */
	public int getNodesCount() {
		return nodes.size();
	}
	
	// ************************************************************************
	// PRIVATE METHODS
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	public Set<Edge> getEdges() {
		return edges;
	}
	
	public Map<Integer, Node> getNodes() {
		return nodes;
	}
}
