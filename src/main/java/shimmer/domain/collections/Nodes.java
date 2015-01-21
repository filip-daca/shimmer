package shimmer.domain.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import shimmer.domain.Node;

public class Nodes {

	// Map with nodes (id -> node)
	private Map<Integer, Node> nodesById;
	
	// Map with node (name -> node)
	private Map<String, Node> nodesByName;
	
	public Nodes() {
		this.nodesById = new LinkedHashMap<Integer, Node>();
		this.nodesByName = new HashMap<String, Node>();
	}
	
	public void add(Node node) {
		node.setId(count());
		nodesById.put(count(), node);
		nodesByName.put(node.getName(), node);
	}
	
	public int count() {
		return nodesById.size();
	}

	public Collection<Node> getCollection() {
		return nodesById.values();
	}
	
	public boolean contains(String nodeName) {
		return nodesByName.containsKey(nodeName);
	}

	public Node get(String name) {
		return nodesByName.get(name);
	}
	
}
