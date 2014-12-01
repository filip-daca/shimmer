package shimmer.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * Class representing a package tree node for faster analysis.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class PackageTreeNode {
	
	// ************************************************************************
	// FIELDS	
	
	private Node lastNode;
	private Map<String, PackageTreeNode> children;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public PackageTreeNode(Node node) {
		this.children = new HashMap<String, PackageTreeNode>();
		this.lastNode = node;
	}
	
	// ************************************************************************
	// METHODS
	
	/**
	 * Finds a parent node for a package.
	 * 
	 * @param packageName - name of a package
	 * @return Node of a parent in a subtree
	 */
	public Node findParent(String packageName) {
		List<String> packageNames = Lists.newArrayList(packageName.split("\\."));
		return findParent(packageNames);
	}
	
	/**
	 * Adds a package node to a tree.
	 * 
	 * @param newNode - node of a new package
	 * @param packageName - name of a package
	 */
	public void addNode(Node newNode, String packageName) {
		List<String> packageNames = Lists.newArrayList(packageName.split("\\."));
		addPackage(newNode, packageNames);
	}

	// ************************************************************************
	// PRIVATE METHODS
	
	/**
	 * Recursively travels down the tree, looking
	 * for a parent node for a package.
	 * 
	 * @param packageNames - path of packages
	 * @return Node of a parent in subtree
	 */
	private Node findParent(List<String> packageNames) {
		if (packageNames.isEmpty()) {
			return lastNode;
		} else {
			PackageTreeNode child = children.get(packageNames.get(0));
			if (child != null) {
				packageNames.remove(0);
				return child.findParent(packageNames);
			} else {
				return lastNode;
			}
		}
	}

	/**
	 * recursively travels down the tree, adding a package.
	 * 
	 * @param newNode
	 * @param packageNames
	 */
	private void addPackage(Node newNode, List<String> packageNames) {
		if (packageNames.isEmpty()) {
			this.lastNode = newNode;
		} else {
			String childPackageName = packageNames.get(0);
			PackageTreeNode child = children.get(childPackageName);
			packageNames.remove(0);
			if (child != null) {
				child.addPackage(newNode, packageNames);
			} else {
				child = new PackageTreeNode(this.lastNode);
				children.put(childPackageName, child);
				child.addPackage(newNode, packageNames);
			}
		}
	}
	
}
