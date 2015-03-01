package shimmer.domain.factory;

import shimmer.domain.Edge;
import shimmer.domain.Node;
import shimmer.enums.EdgeType;

/**
 * Factory helps building new Edges.
 * 
 * @author Filip Daca
 */
public class EdgeFactory {
	
	// ************************************************************************
	// METHODS
	
	public static Edge newPackageTreeEdge(Node node1, Node node2) {
		Edge newEdge = new Edge(node1, node2);
		newEdge.setEdgeType(EdgeType.PACKAGE_EDGE);
		return newEdge;
	}
	
	public static Edge newDependencyEdge(Node node1, Node node2, Integer includeCount) {
		Edge newEdge = new Edge(node1, node2);
		newEdge.setEdgeType(EdgeType.DEPENDENCY_EDGE);
		newEdge.setStrength(includeCount);
		return newEdge;
	}
}
