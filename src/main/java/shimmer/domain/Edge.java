package shimmer.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Shimmer graph edge.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class Edge {

	private Node nodeFrom;
	private Node nodeTo;
	
	public Edge(Node nodeFrom, Node nodeTo) {
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
	}
	
}
