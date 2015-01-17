package shimmer.service.impl;

import javax.inject.Named;

import shimmer.domain.Graph;
import shimmer.domain.Node;
import shimmer.domain.SimulationProperties;
import shimmer.enums.Metric;
import shimmer.service.MetricsService;

/**
 * Standard implementation of methods to operate with basic metrics.
 * 
 * @author filip.daca@javatech.com.pl
 */
@Named
public class MetricsServiceImpl implements MetricsService {
	
	// ************************************************************************
	// IMPLEMENTATIONS
	
	@Override
	public void calculateMetrics(Graph graph, SimulationProperties properties) {
		for (Node node : graph.getNodes()) {
			if (instabilityCalculationRequired(properties)) {
				calculateInstability(node);
			}
			
			if (abstractnessCalculationRequired(properties)) {
				calculateAbstractness(node);
			}
			
			if (distanceCalculationRequired(properties)) {
				calculateDistance(node);
			}
		}
	}
	
	// ************************************************************************
	// PRIVATE METHODS
	
	private void calculateDistance(Node node) {
		node.setDistanceFromMainSequence((node.getAbstractness() + node.getInstability()) / (float) 2);
	}

	private void calculateAbstractness(Node node) {
		if (node.getClassCount() > 0) {
			node.setAbstractness((float) node.getAfferentsCount() / (float) node.getClassCount());
		}
	}

	private void calculateInstability(Node node) {
		if (node.getEfferentsCount() + node.getAfferentsCount() > 0) {
			node.setInstability((float) node.getEfferentsCount() 
					/ (float) (node.getEfferentsCount() + node.getAfferentsCount()));
		}
	}
	
	private boolean isMetricCalculationRequired(SimulationProperties properties, Metric metric) {
		return properties.getNodeColorMetric() == metric
				|| properties.getNodeHeatMetric() == metric
				|| properties.getNodeSizeMetric() == metric;
	}

	private boolean distanceCalculationRequired(SimulationProperties properties) {
		return isMetricCalculationRequired(properties, Metric.DISTANCE_FROM_MAIN_SEQUENCE);
	}

	private boolean abstractnessCalculationRequired(
			SimulationProperties properties) {
		return distanceCalculationRequired(properties)
				|| isMetricCalculationRequired(properties, Metric.ABSTRACTNESS);
	}

	private boolean instabilityCalculationRequired(
			SimulationProperties properties) {
		return distanceCalculationRequired(properties)
				|| isMetricCalculationRequired(properties, Metric.INSTABILITY);
	}
	
}
