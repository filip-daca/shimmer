package shimmer.domain.collections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import shimmer.domain.Node;

public class Nodes {

	// Map with nodes (id -> node)
	private Map<Integer, Node> nodes;
	
	public Nodes() {
		this.nodes = new LinkedHashMap<Integer, Node>();
	}
	
	public void add(Node node) {
		node.setId(count());
		nodes.put(count(), node);
	}
	
	public int count() {
		return nodes.size();
	}

	public Collection<Node> getCollection() {
		return nodes.values();
	}
	
}
