package shimmer.service.impl;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.json.simple.parser.JSONParser;
import org.omnifaces.util.Messages;
import org.primefaces.json.JSONObject;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import shimmer.service.FileService;

@Named
public class FileServiceImpl implements FileService {
	
	// ************************************************************************
	// STATIC FIELDS
		
	private static final String TEMPORARY_JSON_FILE = "graph.json";

	// ************************************************************************
	// TOOLS
	
	@SuppressWarnings("cdi-ambiguous-dependency")
	@Inject
	@Named("fileProperties")
	private Properties fileProperties;
	
	// ************************************************************************
	// IMPLEMENTATIONS

	@Override
	public StreamedContent saveGraph(String nodesJSON, String edgesJSON) {
		JSONObject jsonObject = new JSONObject();
		String temporaryFilePath = fileProperties.getProperty("fileSystem.temp") + TEMPORARY_JSON_FILE;
		try {
			JSONParser parser = new JSONParser();
			jsonObject.put("nodes", parser.parse(nodesJSON));
			jsonObject.put("edges", parser.parse(edgesJSON));
			if (!writeJSONToFile(jsonObject, temporaryFilePath)) {
				Messages.addGlobalError("Unable to save JSON file.");
				return null;
			}
			File jsonFile = new File(temporaryFilePath);
			InputStream inputStream = new FileInputStream(jsonFile);
			StreamedContent savedGraph = new DefaultStreamedContent(inputStream, "application/javascript", "shimmer.json");
			return savedGraph;
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Unable to write JSON file to browser.");
			return null;
		}
	}	
	
	// ************************************************************************
	// HELPER METHORS	
	
	/**
	 * Writes json object to file
	 * @param json - json
	 * @param filePath - path to file
	 * @return yes / no - success
	 */
	private boolean writeJSONToFile(JSONObject json, String filePath) {
		FileWriter file = null;
		try {
			file = new FileWriter(filePath);
			file.write(json.toString(4));
			file.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close(file);
		}
	}
	
	/**
	 * Closes resource
	 * @param c - resource
	 */
	private void close(Closeable c) {
		if (c == null) return; 
		try {
		    c.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
