package shimmer.domain;

import shimmer.enums.Metric;

/**
 * Class represents a set of settigs for Shimmer simulation.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class SimulationProperties {

	private String directoryPath;
	private Metric nodeSizeMetric;
	private Metric nodeColorMetric;
	private Metric nodeHeatMetric;
	
	/**
	 * Show package tree edges?
	 */
	private boolean packageTreeEdges;
	
	/**
	 * Show dependencies edges?
	 */
	private boolean dependenciesEdges;
	
	/**
	 * Visualize extra package tree nodes?
	 */
	private boolean fullPackageTree;
	
	/**
	 * Visualize included library packages?
	 */
	private boolean libraryPackages;
	
	private boolean constellation;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public SimulationProperties() {
		this.directoryPath = "C:\\Users\\9470m\\git\\shimmer\\target\\classes";
		this.nodeSizeMetric = Metric.CLASS_COUNT;
		this.nodeColorMetric = Metric.DISTANCE_FROM_MAIN_SEQUENCE;
		this.nodeHeatMetric = Metric.ABSTRACTNESS;
		this.packageTreeEdges = true;
		this.dependenciesEdges = false;
		this.fullPackageTree = false;
		this.libraryPackages = true;
		this.constellation = true;
	}
	
	// ************************************************************************
	// GETTERS / SETTERS
	
	public Metric getNodeColorMetric() {
		return nodeColorMetric;
	}
	
	public void setNodeColorMetric(Metric nodeColorMetric) {
		this.nodeColorMetric = nodeColorMetric;
	}
	
	public Metric getNodeHeatMetric() {
		return nodeHeatMetric;
	}
	
	public void setNodeHeatMetric(Metric nodeHeatMetric) {
		this.nodeHeatMetric = nodeHeatMetric;
	}
	
	public Metric getNodeSizeMetric() {
		return nodeSizeMetric;
	}
	
	public void setNodeSizeMetric(Metric nodeSizeMetric) {
		this.nodeSizeMetric = nodeSizeMetric;
	}
	
	public String getDirectoryPath() {
		return directoryPath;
	}
	
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	
	public boolean isPackageTreeEdges() {
		return packageTreeEdges;
	}
	
	public void setPackageTreeEdges(boolean packageTreeEdges) {
		this.packageTreeEdges = packageTreeEdges;
	}
	
	public boolean isFullPackageTree() {
		return fullPackageTree;
	}
	
	public void setFullPackageTree(boolean fullPackageTree) {
		this.fullPackageTree = fullPackageTree;
	}
	
	public boolean isLibraryPackages() {
		return libraryPackages;
	}
	
	public void setLibraryPackages(boolean libraryPackages) {
		this.libraryPackages = libraryPackages;
	}
	
	public boolean isDependenciesEdges() {
		return dependenciesEdges;
	}
	
	public void setDependenciesEdges(boolean dependenciesEdges) {
		this.dependenciesEdges = dependenciesEdges;
	}
	
	public boolean isConstellation() {
		return constellation;
	}
	
	public void setConstellation(boolean constellation) {
		this.constellation = constellation;
	}
	
}
