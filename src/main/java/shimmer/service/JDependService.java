package shimmer.service;

import shimmer.domain.Graph;

/**
 * Service provides methods to operate with Findbugs Library.
 * 
 * @author Filip Daca
 */
public interface JDependService {

	/**
	 * Runs JDepend module and generates shimmer Graph.
	 * 
	 * @param directoryName - name of project directory
	 * @param packageTreeEdges - build package tree edges?
	 * @param dependenciesEdges - build dependencies edges?
	 * @param fullPackageTree - build extra package tree nodes?
	 * @param libraryPackages - include library packages?
	 * 
	 * @return project graph
	 */
	Graph generateGraph(String directoryName, boolean packageTreeEdges,
			boolean dependenciesEdges, boolean fullPackageTree, 
			boolean libraryPackages);

}
