package shimmer.domain;

import shimmer.enums.EdgeType;

/**
 * Class representing a Shimmer graph edge.
 * 
 * @author filip.daca@javatech.com.pl
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
