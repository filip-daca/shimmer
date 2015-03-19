package shimmer.domain;

import shimmer.enums.EdgeType;

/**
 * Class representing a Shimmer graph edge.
 * 
 * @author Filip Daca
 */
public class Edge {

	// ************************************************************************
	// FIELDS	
	
	private Node nodeFrom;
	private Node nodeTo;
	
	private int strength;
	
	private EdgeType edgeType;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Edge(Node nodeFrom, Node nodeTo) {
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
		this.strength = 1;
	}
	
	// ************************************************************************
	// METHODS
	
	/**
	 * Is this an edge with a library package?
	 * @return true / false
	 */
	public boolean hasLibrary() {
		return nodeFrom.isLibraryPackage() || nodeTo.isLibraryPackage();
	}
	
	/**
	 * Is this an edges with a directory node?
	 * @return true / false
	 */
	public boolean hasDirectory() {
		return nodeFrom.isDirectory() || nodeTo.isDirectory();
	}
	
	public boolean isDependencyEdge() {
		return edgeType.equals(EdgeType.DEPENDENCY_EDGE);
	}
	
	public boolean isPackageEdge() {
		return edgeType.equals(EdgeType.PACKAGE_EDGE);
	}
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	public Node getNodeFrom() {
		return nodeFrom;
	}
	
	public Node getNodeTo() {
		return nodeTo;
	}
	
	public int getStrength() {
		return strength;
	}
	
	public void setStrength(int strength) {
		this.strength = strength;
	}
	
	public EdgeType getEdgeType() {
		return edgeType;
	}
	
	public void setEdgeType(EdgeType edgeType) {
		this.edgeType = edgeType;
	}
	
}
