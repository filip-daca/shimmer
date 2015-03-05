package shimmer.service.impl;

import javax.inject.Named;

import shimmer.domain.Graph;
import shimmer.domain.Node;
import shimmer.service.MetricsService;

/**
 * Standard implementation of methods to operate with basic metrics.
 * 
 * @author Filip Daca
 */
@Named
public class MetricsServiceImpl implements MetricsService {
	
	// ************************************************************************
	// IMPLEMENTATIONS
	
	@Override
	public void calculateMetrics(Graph graph) {
		for (Node node : graph.getNodes()) {
			calculateInstability(node);
			calculateAbstractness(node);
			calculateDistance(node);
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
	
}
