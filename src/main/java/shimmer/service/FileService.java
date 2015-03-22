package shimmer.service;

import java.io.File;
import java.util.List;

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

	/**
	 * Prepares a directory path - creates all directories or removes
	 * content when its occupied.
	 * @param path - path to prepare
	 * @return file handler
	 */
	File prepareDirectory(String path);

	/**
	 * Deletes directory and all contents.
	 * @param path - path to delete
	 */
	void delete(String path);

	/**
	 * Lists all *.classFiles
	 * @param directory - directory to search
	 * @return list of all .class files in all subdirectories
	 */
	List<File> listClassFiles(File directory);

}
