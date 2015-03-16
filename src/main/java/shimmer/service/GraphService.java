package shimmer.service;

import org.primefaces.json.JSONObject;

import shimmer.domain.Graph;
import shimmer.domain.SimulationProperties;

/**
 * Service provides methods to operate with Graphs.
 * 
 * @author Filip Daca
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
	/**
	 * Outputs shimmer serialized data in JSON format. 
	 * 
	 * @param nodesJSON - stringified nodes data
	 * @param edgesJSON - stringified edges data
	 * @param properties - simulation properties
	 * @return serialized simulation
	 */
	JSONObject generateShimmerJSON(String nodesJSON, String edgesJSON,
			SimulationProperties properties);
}
