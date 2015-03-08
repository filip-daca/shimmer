package shimmer.domain;

import java.util.ArrayList;
import java.util.List;

import shimmer.enums.NodeType;

/**
 * Class representing a Shimmer graph node.
 * 
 * @author Filip Daca
 */
public class Node {
	
	// ************************************************************************
	// FIELDS	
	
	// Basic fields
	private int id;
	private String name;
	private List<Edge> edges;
	private NodeType nodeType;
	
	// Package specific fields
	private int totalSize;
	private int classCount;
	private int abstractClassesCount;
	private int concreteClassesCount;
	private int afferentsCount;
	private int efferentsCount;
	
	// Metrics results 
	private int largestClassSize;
	private float abstractness;
	private float instability;
	private float distanceFromMainSequence;
	private float averageSize;
	
	// Bugs
	private List<Bug> bugs;
	private int totalBugs;
	private int[] priorityBugs;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Node() {
		this.edges = new ArrayList<Edge>();
		this.bugs = new ArrayList<Bug>();
		this.priorityBugs = new int[6];
	}
	
	public Node(String name) {
		this();
		this.name = name;
	}
	
	public Node(String name, NodeType type) {
		this(name);
		this.nodeType = type;
	}
	
	public Node(String name, int classCount, int abstractClassesCount,
			int concreteClassesCount, int efferentsCount, int afferentsCount) {
		this(name, NodeType.ANALYSED_PACKAGE);
		this.classCount = classCount;
		this.abstractClassesCount = abstractClassesCount;
		this.concreteClassesCount = concreteClassesCount;
		this.afferentsCount = afferentsCount;
		this.efferentsCount = efferentsCount;
	}
	
	public Node(String name, int efferentsCount, int afferentsCount) {
		this(name, NodeType.LIBRARY_PACKAGE);
		this.afferentsCount = afferentsCount;
		this.efferentsCount = efferentsCount;
	}
	
	// ************************************************************************
	// METHODS

	@Override
	public String toString() {
		return getName();
	}
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public float getAbstractness() {
		return abstractness;
	}
	
	public void setAbstractness(float abstractness) {
		this.abstractness = abstractness;
	}
	
	public float getDistanceFromMainSequence() {
		return distanceFromMainSequence;
	}
	
	public void setDistanceFromMainSequence(float distanceFromMainSequence) {
		this.distanceFromMainSequence = distanceFromMainSequence;
	}
	
	public float getInstability() {
		return instability;
	}
	
	public void setInstability(float instability) {
		this.instability = instability;
	}
	
	public int getAbstractClassesCount() {
		return abstractClassesCount;
	}
	
	public int getConcreteClassesCount() {
		return concreteClassesCount;
	}
	
	public int getClassCount() {
		return classCount;
	}
	
	public int getAfferentsCount() {
		return afferentsCount;
	}
	
	public int getEfferentsCount() {
		return efferentsCount;
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}
	
	public List<Bug> getBugs() {
		return bugs;
	}
	
	public int getPriorityBugs(int priority) {
		return priorityBugs[priority];
	}
	
	public void setPriorityBugs(int priority, int numberOfBugs) {
		this.priorityBugs[priority] = numberOfBugs;
	}
	
	public int getTotalBugs() {
		return totalBugs;
	}
	
	public void setTotalBugs(int totalBugs) {
		this.totalBugs = totalBugs;
	}
	
	public int getTotalSize() {
		return totalSize;
	}
	
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	
	public float getAverageSize() {
		return averageSize;
	}
	
	public void setAverageSize(float averageSize) {
		this.averageSize = averageSize;
	}
	
	public int getLargestClassSize() {
		return largestClassSize;
	}
	
	public void setLargestClassSize(int largestClassSize) {
		this.largestClassSize = largestClassSize;
	}
}
