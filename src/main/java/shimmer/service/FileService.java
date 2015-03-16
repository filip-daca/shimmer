package shimmer.service;

import org.primefaces.model.StreamedContent;

public interface FileService {

	StreamedContent saveGraph(String nodesJSON, String edgesJSON);

}
