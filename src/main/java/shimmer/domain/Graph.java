package shimmer.domain;

import java.util.Collection;
import java.util.Date;

import shimmer.domain.collections.Edges;
import shimmer.domain.collections.Nodes;
import shimmer.domain.factory.EdgeFactory;
import shimmer.domain.helper.NamesHelper;

/**
 * Class representing a Shimmer graph.
 * 
 * @author Filip Daca
 */
public class Graph {

	// ************************************************************************
	// FIELDS
	
	// Package structure - only used when package tree is needed
	private PackageTreeNode packageRoot;
	
	// Map with nodes (id -> node)
	private Nodes nodes;
	
	// Set with edges
	private Edges edges;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Graph() {
		this.packageRoot = new PackageTreeNode(null);
		this.nodes = new Nodes();
		this.edges = new Edges();
	}
	
	// ************************************************************************
	// METHODS

	/**
	 * Adds a node treated as package.
	 * 
	 * @param analysedPackageNode - package node to add
	 */
	public void addAnalysedPackageNode(Node analysedPackageNode) {
		
		// Adding new node to data collections
		nodes.add(analysedPackageNode);
		
		packageRoot.addJoint(nodes, analysedPackageNode, analysedPackageNode.getName());
	}
	
	/**
	 * Generates edges of a package tree.
	 */
	public void generateTreeEdges() {
		for (PackageTreeNode packageTreeNode : packageRoot.getChildren().values()) {
			packageTreeNode.generateEdges(edges);
		}
	}
	
	/**
	 * Adds an efferent edge
	 * @param package1Name - name of package 1
	 * @param package2Name - name of package 2
	 * @param includeCount - number of iclusions
	 */
	public void addEfferentEdge(String package1Name, String package2Name, Integer includeCount) {
		Node packageNode1 = nodes.get(package1Name);
		Node packageNode2 = nodes.get(package2Name);
		edges.add(EdgeFactory.newDependencyEdge(packageNode1, packageNode2, includeCount));
	}
	
	/**
	 * Adds a bug information
	 * @param bug - bug instance
	 * @param className - name of class
	 */
	public void addBug(Bug bug, String className) {
		String packageName = className.substring(0, className.lastIndexOf('.'));
		Node packageNode = nodes.get(packageName);
		packageNode.getBugs().add(bug);
	}
	
	/**
	 * Applies Findbugs information about package.
	 * @param packageName - name of package
	 * @param totalBugs - total number of bugs
	 * @param totalSize - total lines of code
	 * @param largestClassSize - size of largest class
	 * @param priorityBugs - array with bugs count of each priority
	 */
	public void applyFindbugsPackageAnalysis(String packageName, int totalBugs,
			int totalSize, int largestClassSize, int[] priorityBugs) {
		Node packageNode = nodes.get(packageName);
		for (int i = 1; i < priorityBugs.length; i++) {
			packageNode.setPriorityBugs(i, priorityBugs[i]);
		}
		packageNode.setTotalBugs(totalBugs);
		packageNode.setTotalSize(totalSize);
		packageNode.setLargestClassSize(largestClassSize);
	}
	
	/**
	 * Does this file path represent a package analysed in shimmer?
	 * @param path - path to file
	 * @return yes / no
	 */
	public boolean isFileRepresented(String path) {
		return getNodeFromFilePath(path) != null;
	}
	
	/**
	 * Applies jGit commit information.
	 * @param path - path to commited file
	 * @param date - date of commit
	 * @param author - author of commit
	 */
	public void noticeCommit(String path, Date date, String author) {
		Node analysedPackage = getNodeFromFilePath(path);
		analysedPackage.addAuthor(author);
		analysedPackage.noticeCommit(date);
	}

	// ************************************************************************
	// PRIVATE METHODS
	
	/**
	 * Tries to find a package represented by the given file path.
	 * @param path - path to file
	 * @return node or null
	 */
	private Node getNodeFromFilePath(String path) {
		for (String potentialPackageName : NamesHelper.packageNamesFromFilePath(path)) {
			if (nodes.get(potentialPackageName) != null) {
				return nodes.get(potentialPackageName);
			}
		}
		return null;
	}
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	/**
	 * @return number of nodes
	 */
	public int getNodesCount() {
		return nodes.count();
	}
	
	/**
	 * @return collection od edges
	 */
	public Collection<Edge> getEdges() {
		return edges.getCollection();
	}

	/**
	 * @return collection of nodes
	 */
	public Collection<Node> getNodes() {
		return nodes.getCollection();
	}
}
