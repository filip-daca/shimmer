package shimmer.domain.collections;

import java.util.LinkedHashSet;
import java.util.Set;

import shimmer.domain.Edge;

public class Edges {

	private Set<Edge> edges;
	
	public Edges() {
		this.edges = new LinkedHashSet<Edge>();
	}

	public Set<Edge> getCollection() {
		return edges;
	}

	public void add(Edge newEdge) {
		edges.add(newEdge);
	}
}
