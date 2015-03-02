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
	private int classCount;
	private int abstractClassesCount;
	private int concreteClassesCount;
	private int afferentsCount;
	private int efferentsCount;
	
	// Metrics results 
	private float abstractness;
	private float instability;
	private float distanceFromMainSequence;
	
	// Bugs
	private List<Bug> bugs;
	private int priority1Bugs;
	private int priority2Bugs;
	private int priority3Bugs;
	private int priority4Bugs;
	private int priority5Bugs;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Node() {
		this.edges = new ArrayList<Edge>();
		this.bugs = new ArrayList<Bug>();
		this.priority1Bugs = 0;
		this.priority2Bugs = 0;
		this.priority3Bugs = 0;
		this.priority4Bugs = 0;
		this.priority5Bugs = 0;
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
	
	public void setPriority1Bugs(int priority1Bugs) {
		this.priority1Bugs = priority1Bugs;
	}
	
	public int getPriority1Bugs() {
		return priority1Bugs;
	}
	
	public void setPriority2Bugs(int priority2Bugs) {
		this.priority2Bugs = priority2Bugs;
	}
	
	public int getPriority2Bugs() {
		return priority2Bugs;
	}
	
	public void setPriority3Bugs(int priority3Bugs) {
		this.priority3Bugs = priority3Bugs;
	}
	
	public int getPriority3Bugs() {
		return priority3Bugs;
	}
	
	public void setPriority4Bugs(int priority4Bugs) {
		this.priority4Bugs = priority4Bugs;
	}
	
	public int getPriority4Bugs() {
		return priority4Bugs;
	}
	
	public void setPriority5Bugs(int priority5Bugs) {
		this.priority5Bugs = priority5Bugs;
	}
	
	public int getPriority5Bugs() {
		return priority5Bugs;
	}
	
}
