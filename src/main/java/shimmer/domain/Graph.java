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

	// Map with nodes (id -> node)
	private Map<Integer, Node> nodes;
	
	// Set with edges
	private Set<Edge> edges;
	
	public Graph() { 
		this.nodes = new LinkedHashMap<Integer, Node>();
		this.edges = new LinkedHashSet<Edge>();
	}
	
	/**
	 * Adds a node with a tree manner.
	 * 
	 * @param parentId
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
	
}
