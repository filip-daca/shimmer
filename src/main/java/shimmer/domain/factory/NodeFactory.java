package shimmer.domain.factory;

import shimmer.domain.Node;
import shimmer.enums.NodeType;

/**
 * Factory helps building new Nodes.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class NodeFactory {
	
	// ************************************************************************
	// METHODS
	
	public static Node newAnalysedPackageNode(String name, int classCount, int abstractClassesCount,
			int concreteClassCount, int efferentsCount, int afferentsCount) {
		return new Node(name, classCount, abstractClassesCount, 
			concreteClassCount, efferentsCount, afferentsCount);
	}
	
	public static Node newTreeNode(String name) {
		return new Node(name, NodeType.TREE_NODE);
	}

	public static Node newLibraryPackageNode(String name, int efferentsCount, int afferentsCount) {
		return new Node(name, efferentsCount, afferentsCount);
	}
}
