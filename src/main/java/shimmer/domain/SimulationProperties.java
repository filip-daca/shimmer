package shimmer.domain;

import shimmer.enums.Metric;

/**
 * Class represents a set of settigs for Shimmer simulation.
 * 
 * @author Filip Daca
 */
public class SimulationProperties {

	// ************************************************************************
	// FIELDS
	
	private String directoryPath;
	private String gitUrl;
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
	private boolean directoryNodes;
	
	/**
	 * Visualize included library packages?
	 */
	private boolean libraryPackages;
	
	/**
	 * Add weight information about depencendy edge?
	 */
	private boolean dependenciesWeighted;
	
	private boolean jGitRequired;
	private boolean findbugsRequired;
	
	private boolean constellation;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public SimulationProperties() {
		this.directoryPath = "C:\\Users\\9470m\\git\\shimmer\\target\\classes";
		this.gitUrl = "https://github.com/filip-daca/shimmer.git";
		this.nodeSizeMetric = Metric.CLASS_COUNT;
		this.nodeColorMetric = Metric.TOTAL_BUGS;
		this.nodeHeatMetric = Metric.DISTANCE_FROM_MAIN_SEQUENCE;
		this.packageTreeEdges = true;
		this.dependenciesEdges = false;
		this.directoryNodes = false;
		this.libraryPackages = true;
		this.constellation = true;
		this.dependenciesWeighted = true;
		this.findbugsRequired = true;
		this.jGitRequired = true;
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
	
	public boolean isDirectoryNodes() {
		return directoryNodes;
	}
	
	public void setDirectoryNodes(boolean directoryNodes) {
		this.directoryNodes = directoryNodes;
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
	
	public boolean isDependenciesWeighted() {
		return dependenciesWeighted;
	}
	
	public void setDependenciesWeighted(boolean dependenciesWeighted) {
		this.dependenciesWeighted = dependenciesWeighted;
	}
	
	public String getGitUrl() {
		return gitUrl;
	}
	
	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}
	
	public boolean isFindbugsRequired() {
		return findbugsRequired;
	}
	
	public void setFindbugsRequired(boolean findbugsRequired) {
		this.findbugsRequired = findbugsRequired;
	}
	
	public boolean isjGitRequired() {
		return jGitRequired;
	}
	
	public void setjGitRequired(boolean jGitRequired) {
		this.jGitRequired = jGitRequired;
	}
}
