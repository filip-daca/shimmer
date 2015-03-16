package shimmer.service;

import org.primefaces.json.JSONObject;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 * Simple service to handle file operations.
 * @author filip.daca@javatech.com.pl
 */
public interface FileService {

	/**
	 * Saves graph to file and returns a streamed content object
	 * to provide download.
	 * @param shimmerJSON - simulation data to save
	 * @return streamed content
	 */
	StreamedContent saveGraph(JSONObject shimmerJSON);

	/**
	 * Loads shimmer graph from file in JSON format
	 * @param file - file with saved shimmer simulation
	 * @return shimmer JSON object
	 */
	JSONObject loadGraph(UploadedFile file);
}
