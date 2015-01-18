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
 * @author filip.daca@javatech.com.pl
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
	
	private boolean initialized = false;
	private SimulationProperties properties;
	private Graph graph;
	private String edgesJSON;
	private String nodesJSON;
	
	private Integer loadingProgress;
	
	// ************************************************************************
	// PERSISTANCE AND INITIALIZATION
	
	@PostConstruct
	public void initialize() {
		// Create default properties
		properties = new SimulationProperties();

		generateGraph();
		initialized = true;
	}
	
	/**
	 * Generates graph and JSON elements from properties
	 */
	public void generateGraph() {
		setLoadindProgress(5);
		graph = jDependService.generateGraph(properties.getDirectoryPath(), 
			properties.isPackageTreeEdges(), properties.isDependenciesEdges(),
			properties.isFullPackageTree(), properties.isLibraryPackages());
		setLoadindProgress(20);
		metricsService.calculateMetrics(graph, properties);
		setLoadindProgress(40);
		findbugsService.applyAnalysis(graph, properties);
		setLoadindProgress(60);
		edgesJSON = graphService.generateEdgesJSON(graph, properties);
		setLoadindProgress(80);
		nodesJSON = graphService.generateNodesJSON(graph, properties);
		setLoadindProgress(100);
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
	
	// ************************************************************************
	// VIEW METHODS
	
	public String getEdgesJSON() {
		considerInitialization();
		return edgesJSON;
	}
	
	public String getNodesJSON() {
		considerInitialization();
		return nodesJSON;
	}
	
	public int getNodesCount() {
		considerInitialization();
		return graph.getNodesCount();
	}
	
	public SimulationProperties getProperties() {
		considerInitialization();
		return properties;
	}
	
	public synchronized int getLoadingProgress() {
		if (loadingProgress == null) {
			return 0;
		} else if (loadingProgress == 100) {
			loadingProgress = null;
			return 100;
		} else {
			return loadingProgress;
		}
	}
	
}