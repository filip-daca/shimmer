package shimmer.domain.factory;

import shimmer.domain.Node;
import shimmer.domain.PackageTreeNode;

/**
 * Factory to create PackageTreeNodes
 * 
 * @author Filip Daca
 */
public class PackageTreeNodeFactory {

	public static PackageTreeNode newRoot() {
		return new PackageTreeNode(null);
	}

	public static PackageTreeNode newJoint(Node childNode) {
		return new PackageTreeNode(childNode);
	}

	public static PackageTreeNode newEmptyJoint() {
		return new PackageTreeNode(null);
	}
	
}
