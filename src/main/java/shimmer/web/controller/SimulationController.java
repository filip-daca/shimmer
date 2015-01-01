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
	
	// ************************************************************************
	// PERSISTANCE AND INITIALIZATION
	
	@PostConstruct
	public void initialize() {
		// Create default properties
		properties = new SimulationProperties();
		
		graph = jDependService.generateGraph(properties.getDirectoryPath());
		metricsService.calculateMetrics(graph, properties);
		findbugsService.applyAnalysis(graph, properties);
		edgesJSON = graphService.generateEdgesJSON(graph, properties);
		nodesJSON = graphService.generateNodesJSON(graph, properties);
		initialized = true;
	}
	
	private void considerInitialization() {
		if (!initialized) {
			initialize();
		}
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
	
}