package shimmer.service;

import shimmer.domain.Graph;

/**
 * Service provides methods to operate with Findbugs Library.
 * 
 * @author filip.daca@javatech.com.pl
 */
public interface JDependService {

	/**
	 * Runs JDepend module and generates shimmer Graph.
	 * 
	 * @param directoryName - name of project directory
	 * @return project graph
	 */
	Graph generateGraph(String directoryName);

}
