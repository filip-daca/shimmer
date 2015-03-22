package shimmer.service.impl;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.json.simple.parser.JSONParser;
import org.omnifaces.util.Messages;
import org.primefaces.json.JSONObject;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

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
	public StreamedContent saveGraph(JSONObject shimmerJSON) {
		String temporaryFilePath = fileProperties.getProperty("fileSystem.temp") + TEMPORARY_JSON_FILE;
		try {
			if (!writeJSONToFile(shimmerJSON, temporaryFilePath)) {
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
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject loadGraph(UploadedFile file) {
		JSONParser parser = new JSONParser();
		try {
			Object parsedObj = parser.parse(new InputStreamReader(file.getInputstream()));
			JSONObject jsonObject = new JSONObject((Map<Object, Object>) parsedObj);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Unable to load JSON file");
			return null;
		}
	}
	
	@Override
	public File prepareDirectory(String path) {
		File directory = new File(path);
		delete(directory);
		directory.mkdirs();
		return directory;
	}
	
	@Override
	public void delete(String path) {
		File directory = new File(path);
		delete(directory);
	}
	
	@Override
	public List<File> listClassFiles(File f) {
		List<File> files = new ArrayList<File>();
		if (f.exists()) {
			if (f.isDirectory()) {
				for (File ff : f.listFiles()) {
					if (!ff.getName().startsWith(".")) {
						files.addAll(listClassFiles(ff));
					}
				}
			}
			if (f.getName().endsWith(".java")) {
				files.add(f);
			}
		}
		return files;
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
	
	/**
	 * Deletes directory with all contents
	 * @param f - directory or file handler
	 */
	private void delete(File f) {
		if (!f.exists()) {
			return;
		}
		if (f.isDirectory()) {
			for (File ff : f.listFiles()) {
				delete(ff);
			}
		}
		f.delete();
	}
}
