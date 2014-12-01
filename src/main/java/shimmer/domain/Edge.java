package shimmer.domain;

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
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Edge(Node nodeFrom, Node nodeTo) {
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
	}
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	public Node getNodeFrom() {
		return nodeFrom;
	}
	
	public Node getNodeTo() {
		return nodeTo;
	}
	
}
