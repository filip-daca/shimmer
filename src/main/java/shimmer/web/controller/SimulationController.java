package shimmer.web.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import shimmer.domain.Graph;
import shimmer.domain.SimulationProperties;
import shimmer.service.FindbugsService;
import shimmer.service.GraphService;
import shimmer.service.JDependService;
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
	private GraphService graphService;
	
	// ************************************************************************
	// CONTROLLER FIELDS
	
	private volatile boolean initialized = false;
	private volatile boolean visualizationReady = false;
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
	    		graph = jDependService.generateGraph(properties.getDirectoryPath(), 
	    			properties.isPackageTreeEdges(), properties.isDependenciesEdges(),
	    			properties.isFullPackageTree(), properties.isLibraryPackages());
	    		setLoadindProgress(20);
	    		findbugsService.applyAnalysis(graph, properties.getDirectoryPath());
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
	
}