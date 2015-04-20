package shimmer.service;

import shimmer.domain.Graph;

/**
 * Service provides methods to analyze hirotical metrics.
 * @author Filip Daca
 */
public interface JGitService {

	/**
	 * Runs github analysis to provide historical metrics.
	 * Clones project to local file system.
	 * @param graph - shimmer graph
	 * @param gitUrl - github clone url
	 */
	void cloneAndAnalyse(Graph graph, String gitUrl);

	/**
	 * Runs github analysis to provide historical metrics.
	 * @param repositoryPath - file system path to git repository
	 * @param graph - shimmer graph
	 */
	void analyse(Graph graph, String repositoryPath);

}
