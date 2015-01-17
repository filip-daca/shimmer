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
	private boolean packageTreeEdges;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public SimulationProperties() {
		// TODO: get path from JSF!
		this.directoryPath = "C:\\Users\\9470m\\git\\shimmer\\target\\classes";
		this.nodeSizeMetric = Metric.CLASS_COUNT;
		this.nodeColorMetric = Metric.DISTANCE_FROM_MAIN_SEQUENCE;
		this.nodeHeatMetric = Metric.ABSTRACTNESS;
		this.packageTreeEdges = true;
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
	
}
