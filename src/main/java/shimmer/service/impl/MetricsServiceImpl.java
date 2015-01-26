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

	/**
	 * A = Ac / Cc
	 * @param node
	 */
	private void calculateAbstractness(Node node) {
		if (node.getClassCount() > 0) {
			float classCount = node.getClassCount();
			float abstractClassesCount = node.getAbstractClassesCount();
			float abstractness = abstractClassesCount / classCount;
			node.setAbstractness(abstractness);
		}
	}

	/**
	 * I = Ce / (Ce + Ca)
	 * @param node
	 */
	private void calculateInstability(Node node) {
		float efferentsCount = node.getEfferentsCount();
		float afferentsCount = node.getAfferentsCount();
		
		if (efferentsCount + afferentsCount > 0) {
			float instability = efferentsCount / (efferentsCount + afferentsCount);
			node.setInstability(instability);
		}
	}
	/**
	 * A + I = 1  -> perfect case
	 * D = |A + I - 1|
	 * @param node
	 */
	private void calculateDistance(Node node) {
		float abstractness = node.getAbstractness();
		float instability = node.getInstability();
		float distance = Math.abs(abstractness + instability - 1);
		node.setDistanceFromMainSequence(distance);
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
