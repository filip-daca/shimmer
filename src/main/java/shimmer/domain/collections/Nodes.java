package shimmer.domain.collections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import shimmer.domain.Node;

public class Nodes {
	
	// Map with node (name -> node)
	private Map<String, Node> nodesByName;
	
	public Nodes() {
		this.nodesByName = new LinkedHashMap<String, Node>();
	}
	
	public void add(Node node) {
		node.setId(count());
		nodesByName.put(node.getName(), node);
	}
	
	public int count() {
		return nodesByName.size();
	}

	public Collection<Node> getCollection() {
		return nodesByName.values();
	}
	
	public boolean contains(String nodeName) {
		return nodesByName.containsKey(nodeName);
	}

	public Node get(String name) {
		return nodesByName.get(name);
	}

	public void addAll(Collection<Node> collection) {
		for (Node node : collection) {
			add(node);
		}
	}
	
}
