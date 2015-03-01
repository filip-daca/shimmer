package shimmer.service;

import shimmer.domain.Graph;
import shimmer.domain.SimulationProperties;

/**
 * Service provides methods to operate with basic metrics.
 * 
 * @author Filip Daca
 */
public interface MetricsService {

	/**
	 * Calculates selected metrics.
	 * 
	 * @param graph - Shimmer graph
	 * @param properties - set of properties
	 */
	void calculateMetrics(Graph graph, SimulationProperties properties);

}
