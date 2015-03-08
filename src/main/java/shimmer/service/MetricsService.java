package shimmer.service;

import shimmer.domain.Graph;

/**
 * Service provides methods to operate with basic metrics.
 * 
 * @author filip.daca@javatech.com.pl
 */
public interface MetricsService {

	/**
	 * Calculates selected metrics.
	 * 
	 * @param graph - Shimmer graph
	 */
	void calculateMetrics(Graph graph);

}
