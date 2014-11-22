package shimmer.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Shimmer graph node.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class Node {
	
	private int id;

	private List<Edge> edges;
	
	public Node(int id) {
		this.edges = new ArrayList<Edge>();
		this.id = id;
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public int getId() {
		return id;
	}
	
}
