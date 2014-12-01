package shimmer.service;

import shimmer.domain.Graph;
import shimmer.domain.SimulationProperties;

/**
 * Service provides methods to operate with Graphs.
 * 
 * @author filip.daca@javatech.com.pl
 */
public interface GraphService {

	/**
	 * Outputs graph edges in JSON format.
	 * 
	 * @param graph - project graph
	 * @param properties - simulation properties
	 * @return edges in JSON format
	 */
	String generateEdgesJSON(Graph graph, SimulationProperties properties);

	/**
	 * Outputs graph nodes in JSON format.
	 * 
	 * @param graph - project graph
	 * @param properties - simulation properties
	 * @return nodes in JSON format
	 */
	String generateNodesJSON(Graph graph, SimulationProperties properties);

}
