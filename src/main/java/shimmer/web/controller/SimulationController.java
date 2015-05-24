package shimmer.web.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import shimmer.domain.Graph;
import shimmer.domain.MetricProperties;
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
	
	private SimulationProperties simulationProperties;
	private MetricProperties metricProperties;
	
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
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		// Create default properties
		simulationProperties = new SimulationProperties();
		metricProperties = new MetricProperties();
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
	    		graph = jDependService.generateGraph(simulationProperties.getDirectoryPath());
	    		setLoadindProgress(30);
	    		if (simulationProperties.isFindbugsRequired()) {
	    			findbugsService.applyAnalysis(graph, simulationProperties.getDirectoryPath(),
	    					simulationProperties.isFindbugsHighPriority());
	    		}
	    		setLoadindProgress(60);
	    		if (simulationProperties.isjGitRequired()) {
	    			if (StringUtils.hasText(simulationProperties.getGitUrl())) {
	    				jGitService.cloneAndAnalyse(graph, simulationProperties.getGitUrl());
	    			} else if (StringUtils.hasText(simulationProperties.getRepositoryPath())) {
	    				jGitService.analyse(graph, simulationProperties.getRepositoryPath());
	    			}
	    		}
	    		setLoadindProgress(80);
	    		metricsService.calculateMetrics(graph);
	    		setLoadindProgress(90);
	    		edgesJSON = graphService.generateEdgesJSON(graph, simulationProperties);
	    		setLoadindProgress(95);
	    		nodesJSON = graphService.generateNodesJSON(graph, simulationProperties, metricProperties);
	    		setLoadindProgress(100);
	    		setVisualizationReady(true);
	        }
	    };
	    graphGenerationThread.start();
	    propertiesReadonly = false;
	}
	
	/**
	 * Regenerates JSON elements of graph after properties change.
	 */
	public void changeProperties() {	    
	    edgesJSON = graphService.generateEdgesJSON(graph, simulationProperties);
	    nodesJSON = graphService.generateNodesJSON(graph, simulationProperties, metricProperties);
	}
	
	public void loadGraph(FileUploadEvent event) {
		JSONObject shimmerJSON = fileService.loadGraph(event.getFile());
		if (shimmerJSON == null) {
			return;
		}
		try {
			edgesJSON = shimmerJSON.get("edges").toString();
			nodesJSON = shimmerJSON.get("nodes").toString();
			simulationProperties.setDependenciesEdges(shimmerJSON.getBoolean("dependenciesEdges"));
			simulationProperties.setDependenciesWeighted(shimmerJSON.getBoolean("dependenciesWeighted"));
			simulationProperties.setDirectoryNodes(shimmerJSON.getBoolean("directoryNodes"));
			simulationProperties.setLibraryPackages(shimmerJSON.getBoolean("libraryPackages"));
			simulationProperties.setNodeColorMetric(Metric.valueOf(shimmerJSON.getString("nodeColorMetric")));
			simulationProperties.setNodeSizeMetric(Metric.valueOf(shimmerJSON.getString("nodeSizeMetric")));
			simulationProperties.setNodeHeatMetric(Metric.valueOf(shimmerJSON.getString("nodeHeatMetric")));
			setVisualizationReady(true);
			propertiesReadonly = true;
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Unable to parse JSON from file");
		}
    }
	
	public StreamedContent getSaveGraph() {
		JSONObject shimmerJSON = graphService.generateShimmerJSON(nodesJSON, edgesJSON, simulationProperties);
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
	
	public SimulationProperties getSimulationProperties() {
		considerInitialization();
		return simulationProperties;
	}
	
	public MetricProperties getMetricProperties() {
		return metricProperties;
	}
	
	public synchronized int getLoadingProgress() {
		setLoadindProgress(loadingProgress + 1); 
		return loadingProgress;
	}
	
	public boolean isPropertiesReadonly() {
		return propertiesReadonly;
	}
	
}
