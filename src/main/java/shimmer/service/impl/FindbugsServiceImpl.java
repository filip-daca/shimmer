package shimmer.service.impl;

import javax.inject.Named;

import edu.umd.cs.findbugs.FindBugs2;
import shimmer.domain.Graph;
import shimmer.domain.SimulationProperties;
import shimmer.service.FindbugsService;

/**
 * Standard implementation of methods to operate with Findbugs Library.
 * 
 * @author Filip Daca
 */
@Named
public class FindbugsServiceImpl implements FindbugsService {

	@Override
	public void applyAnalysis(Graph graph, SimulationProperties properties) {
		String command = properties.getDirectoryPath() + " -effort:min -textui -relaxed";
		try {
			FindBugs2.main(command.split("\\s+"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
