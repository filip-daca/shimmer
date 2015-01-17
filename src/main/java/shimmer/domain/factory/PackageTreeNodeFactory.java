package shimmer.domain.factory;

import shimmer.domain.Node;
import shimmer.domain.PackageTreeNode;

/**
 * Factory to create PackageTreeNodes
 * 
 * @author filip.daca@javatech.com.pl
 */
public class PackageTreeNodeFactory {

	public static PackageTreeNode newRoot() {
		return new PackageTreeNode(null);
	}

	public static PackageTreeNode newPackageTreeNode(Node childNode) {
		return new PackageTreeNode(childNode);
	}
	
}
