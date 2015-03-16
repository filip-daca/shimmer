package shimmer.web.controller;

import java.io.InputStreamReader;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.runtime.parser.node.SetExecutor;
import org.json.simple.parser.JSONParser;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.StreamedContent;
import org.primefaces.push.impl.JSONDecoder;
import org.primefaces.push.impl.JSONEncoder;
import org.springframework.context.annotation.Scope;


import org.springframework.util.StringUtils;

import shimmer.domain.Graph;
import shimmer.domain.SimulationProperties;
import shimmer.enums.Metric;
import shimmer.service.FileService;
import shimmer.service.FindbugsService;
import shimmer.service.GraphService;
import shimmer.service.JDependService;
import shimmer.service.JGitService;
import shimmer.service.MetricsService;

/**
 * Basic controller for simulation.
 * 
 * @author Filip Daca
 */
@Named
@Scope("view")
public class SimulationController implements Serializable {
	
	// ************************************************************************
	// STATIC FIELDS
	
	private static final long serialVersionUID = 1L;

	// ************************************************************************
	// TOOLS AND SERVICES
	
	@Inject
	private JDependService jDependService;
	
	@Inject
	private MetricsService metricsService;
	
	@Inject
	private FindbugsService findbugsService;
	
	@Inject
	private JGitService jGitService;
	
	@Inject
	private GraphService graphService;
	
	@Inject
	private FileService fileService;
	
	// ************************************************************************
	// CONTROLLER FIELDS
	
	private volatile boolean initialized = false;
	private volatile boolean visualizationReady = false;
	private boolean propertiesReadonly = false;
	
	private SimulationProperties properties;
	private volatile Graph graph;
	private volatile String edgesJSON;
	private volatile String nodesJSON;
	
	private volatile int loadingProgress;
	
	// ************************************************************************
	// THREAD WORK
	
	private Thread graphGenerationThread = new Thread();
	
	// ************************************************************************
	// PERSISTANCE AND INITIALIZATION
	
	@PostConstruct
	public void initialize() {
		// Create default properties
		properties = new SimulationProperties();
		initialized = true;
	}
	
	/**
	 * Generates graph and JSON elements from properties
	 */
	public void generateGraph() {
		// Skip running analysis when work is in progress
		if (graphGenerationThread.isAlive()) {
			return;
		}
		
		setVisualizationReady(false);
		setLoadindProgress(0);
		
		graphGenerationThread = new Thread() {
	        public void run() {
	        	setLoadindProgress(5);
	        	if (StringUtils.hasText(properties.getGitUrl())) {
//	        		String gitRepositoryPath = jGitService.cloneRepository(properties.getGitUrl());
//	        		properties.setDirectoryPath(gitRepositoryPath);
	        	}
	    		graph = jDependService.generateGraph(properties.getDirectoryPath(), 
	    			properties.isPackageTreeEdges(), properties.isDependenciesEdges(),
	    			properties.isFullPackageTree(), properties.isLibraryPackages());
	    		setLoadindProgress(30);
	    		findbugsService.applyAnalysis(graph, properties.getDirectoryPath());
	    		setLoadindProgress(60);
	    		jGitService.applyHistoricalAnalysis(graph, properties.getGitUrl());
	    		setLoadindProgress(80);
	    		metricsService.calculateMetrics(graph);
	    		setLoadindProgress(90);
	    		edgesJSON = graphService.generateEdgesJSON(graph, properties);
	    		setLoadindProgress(95);
	    		nodesJSON = graphService.generateNodesJSON(graph, properties);
	    		setLoadindProgress(100);
	    		setVisualizationReady(true);
	        }
	    };
	    graphGenerationThread.start();
	    propertiesReadonly = false;
	}
	
	public void loadGraph(FileUploadEvent event) {
		JSONObject shimmerJSON = fileService.loadGraph(event.getFile());
		if (shimmerJSON == null) {
			return;
		}
		try {
			edgesJSON = shimmerJSON.get("edges").toString();
			nodesJSON = shimmerJSON.get("nodes").toString();
			properties.setDependenciesEdges(shimmerJSON.getBoolean("dependenciesEdges"));
			properties.setDependenciesWeighted(shimmerJSON.getBoolean("dependenciesWeighted"));
			properties.setFullPackageTree(shimmerJSON.getBoolean("fullPackageTree"));
			properties.setLibraryPackages(shimmerJSON.getBoolean("libraryPackages"));
			properties.setNodeColorMetric(Metric.valueOf(shimmerJSON.getString("nodeColorMetric")));
			properties.setNodeSizeMetric(Metric.valueOf(shimmerJSON.getString("nodeSizeMetric")));
			properties.setNodeHeatMetric(Metric.valueOf(shimmerJSON.getString("nodeHeatMetric")));
			setVisualizationReady(true);
			propertiesReadonly = true;
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Unable to parse JSON from file");
		}
    }
	
	public StreamedContent getSaveGraph() {
		JSONObject shimmerJSON = graphService.generateShimmerJSON(nodesJSON, edgesJSON, properties);
		return fileService.saveGraph(shimmerJSON);
	}
	
	private void considerInitialization() {
		if (!initialized) {
			initialize();
		}
	}
	
	// ************************************************************************
	// LOADING PROGRESS
	
	private synchronized void setLoadindProgress(int value) {
		loadingProgress = value;
	}
	
	public synchronized boolean isVisualizationReady() {
		return visualizationReady;
	}
	
	private synchronized void setVisualizationReady(boolean visualizationReady) {
		this.visualizationReady = visualizationReady;
	}
	
	// ************************************************************************
	// VIEW METHODS
	
	public synchronized String getEdgesJSON() {
		return edgesJSON;
	}
	
	public synchronized String getNodesJSON() {
		return nodesJSON;
	}
	
	public synchronized int getNodesCount() {
		return graph.getNodesCount();
	}
	
	public SimulationProperties getProperties() {
		considerInitialization();
		return properties;
	}
	
	public synchronized int getLoadingProgress() {
		return loadingProgress;
	}
	
	public boolean isPropertiesReadonly() {
		return propertiesReadonly;
	}
	
}