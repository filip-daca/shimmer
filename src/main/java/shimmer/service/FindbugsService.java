package shimmer.service;

import shimmer.domain.Graph;

/**
 * Service provides methods to operate with Findbugs Library.
 * 
 * @author Filip Daca
 */
public interface FindbugsService {

	/**
	 * Reads Findbugs report file and generates Graph object.
	 * 
	 * @param findbugsReportFilename - file name of Findbugs report
	 * @param directoryPath - path to project
	 * @return project graph
	 */
	void applyAnalysis(Graph graph, String directoryPath);

}
