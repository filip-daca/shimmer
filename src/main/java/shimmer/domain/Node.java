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
	
	// Package specific fields
	private NodeType nodeType;
	private int classCount;
	private int abstractClassesCount;
	private int concreteClassesCount;
	private int afferentsCount;
	private int efferentsCount;
	private int volatility;
	
	// Metrics results 
	private float abstractness;
	private float instability;
	private float distanceFromMainSequence;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Node() {
		this.edges = new ArrayList<Edge>();
	}
	
	public Node(int id) {
		this();
		this.id = id;
	}
	
	public Node(int id, String name) {
		this(id);
		this.name = name;
	}
	
	public Node(int id, String name, int classCount, int abstractClassesCount,
			int concreteClassesCount, int volatility, int efferentsCount,
			int afferentsCount) {
		this(id, name);
		this.nodeType = NodeType.PACKAGE;
		this.classCount = classCount;
		this.abstractClassesCount = abstractClassesCount;
		this.concreteClassesCount = concreteClassesCount;
		this.volatility = volatility;
		this.afferentsCount = afferentsCount;
		this.efferentsCount = efferentsCount;
	}
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public float getAbstractness() {
		return abstractness;
	}
	
	public float getDistanceFromMainSequence() {
		return distanceFromMainSequence;
	}
	
	public float getInstability() {
		return instability;
	}
	
	public void setDistanceFromMainSequence(Float distanceFromMainSequence) {
		this.distanceFromMainSequence = distanceFromMainSequence;
	}
	
	public void setInstability(Float instability) {
		this.instability = instability;
	}
	
	public void setAbstractness(Float abstractness) {
		this.abstractness = abstractness;
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
	
}
