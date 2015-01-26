package shimmer.domain;

import java.util.ArrayList;
import java.util.List;

import shimmer.enums.NodeType;

/**
 * Class representing a Shimmer graph node.
 * 
 * @author filip.daca@javatech.com.pl
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
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Node() {
		this.edges = new ArrayList<Edge>();
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
	
}
