package shimmer.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shimmer.domain.collections.Edges;
import shimmer.domain.collections.Nodes;
import shimmer.domain.factory.NodeFactory;
import shimmer.domain.factory.PackageTreeNodeFactory;
import shimmer.domain.helper.GraphHelper;

import com.google.common.collect.Lists;

/**
 * Class representing a package tree node for faster analysis.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class PackageTreeNode {
	
	// ************************************************************************
	// FIELDS	
	
	private Node node;
	private Map<String, PackageTreeNode> children;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public PackageTreeNode(Node node) {
		this.children = new HashMap<String, PackageTreeNode>();
		this.node = node;
	}
	
	// ************************************************************************
	// DIAGNOSTICS
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName());
		sb.append(" ");
		sb.append(getName());
		return sb.toString();
	}
	
	// ************************************************************************
	// METHODS
	
	/**
	 * Adds a package node to a tree.
	 * 
	 * @param newNode - node of a new package
	 * @param packageName - name of a package
	 * @param addExtraTreeNodes - visualize each step in package tree?
	 */
	public void addJoint(Nodes nodes, Node newNode, String packageName, 
			boolean addExtraTreeNodes) {
		
		List<String> packageNames = Lists.newArrayList(packageName.split("\\."));
		addJoint(nodes, newNode, packageNames, addExtraTreeNodes);
	}

	// ************************************************************************
	// PRIVATE METHODS

	/**
	 * Recursively travels down the tree, adding a package.
	 * 
	 * @param nodes - all nodes collection
	 * @param newNode - node to add in leaf
	 * @param packageNames - package names to visit
	 * @param addExtraTreeNodes - visualize each step in package tree?
	 */
	private void addJoint(Nodes nodes, Node newNode, List<String> packageNames,
			boolean addExtraTreeNodes) {
		
		if (packageNames.isEmpty()) {
			this.node = newNode;
		} else {
			// Attempt to go deeper
			String childPackageNameSnippet = packageNames.get(0);
			PackageTreeNode childJoint = children.get(childPackageNameSnippet);
			packageNames.remove(0);
			
			// Need to create a child
			if (childJoint == null) {
				
				if (addExtraTreeNodes) {
					String childPackageName = GraphHelper.getFullPackageName(getName(), childPackageNameSnippet);
					
					Node childNode;
					// There is an analysed package already
					if (nodes.contains(childPackageName)) {
						childNode = nodes.get(childPackageName);
					// Create a tree joint
					} else {
						childNode = NodeFactory.newTreeNode(childPackageName);
						nodes.add(childNode);
					}
					
					childJoint = PackageTreeNodeFactory.newJoint(childNode);
				} else {
					childJoint = PackageTreeNodeFactory.newEmptyJoint();
				}
				
				children.put(childPackageNameSnippet, childJoint);
			}
			
			// Add new package recursively
			childJoint.addJoint(nodes, newNode, packageNames, addExtraTreeNodes);
		}
	}

	/**
	 * Recursively travels down the tree, adding edges.
	 * 
	 * @param edges - all edges collection
	 */
	public void generateEdges(Edges edges) {
		// For every child
		for (PackageTreeNode childJoint : children.values()) {
			Node child = childJoint.node;
		
			if (child != null && node != null) {
				// Create new edge
				Edge newEdge = new Edge(node, childJoint.node);
				edges.add(newEdge);
				node.getEdges().add(newEdge);
				childJoint.node.getEdges().add(newEdge);
			}
			
			// Populate edge creation
			childJoint.generateEdges(edges);
		}
	}
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	public Map<String, PackageTreeNode> getChildren() {
		return children;
	}
	
	public String getName() {
		if (node == null) {
			return "";
		} else {
			return node.getName();
		}
	}

	
	
}
