package shimmer.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private NodeType nodeType;
	private boolean directoryToLibrary;
	
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
	
	// Historical metric
	private int commitsCount;
	private Date lastCommitDate;
	private Set<String> authors;
	
	// Bugs
	private List<Bug> bugs;
	private int totalBugs;
	private int[] priorityBugs;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public Node() {
		this.bugs = new ArrayList<Bug>();
		this.authors = new HashSet<String>();
		this.priorityBugs = new int[6];
		this.directoryToLibrary = false;
	}
	
	public Node(String name) {
		this();
		this.name = name;
	}
	
	public Node(String name, NodeType type) {
		this(name);
		this.nodeType = type;
	}
	
	public Node(String name, NodeType type, boolean directoryToLibrary) {
		this(name);
		this.nodeType = type;
		this.directoryToLibrary = directoryToLibrary;
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
	
	public boolean isLibraryPackage() {
		return nodeType.equals(NodeType.LIBRARY_PACKAGE);
	}
	
	public boolean isDirectory() {
		return nodeType.equals(NodeType.DIRECTORY);
	}
	
	public void addAuthor(String author) {
		authors.add(author);
	}
	
	public void noticeCommit(Date date) {
		commitsCount++;
		if (lastCommitDate == null || lastCommitDate.before(date)) {
			lastCommitDate = date;
		}
	}
	
	// ************************************************************************
	// GETTERS / SETTERS
	
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
	
	public Date getLastCommitDate() {
		return lastCommitDate;
	}
	
	public int getCommitsCount() {
		return commitsCount;
	}
	
	public Set<String> getAuthors() {
		return authors;
	}
	
	public boolean isDirectoryToLibrary() {
		return directoryToLibrary;
	}
}
