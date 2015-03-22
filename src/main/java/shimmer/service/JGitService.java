package shimmer.service;

import shimmer.domain.Graph;

/**
 * Service provides methods to analyze hirotical metrics.
 * @author filip.daca@javatech.com.pl
 */
public interface JGitService {

	/**
	 * Runs github analysis to provide historical metrics.
	 * @param graph - shimmer graph
	 * @param gitUrl - github clone url
	 */
	void applyHistoricalAnalysis(Graph graph, String gitUrl);

}
