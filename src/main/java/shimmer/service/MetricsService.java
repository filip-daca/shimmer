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
	 */
	void calculateMetrics(Graph graph);

}
