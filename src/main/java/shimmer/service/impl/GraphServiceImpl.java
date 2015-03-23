package shimmer.service.impl;

import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.apache.commons.lang.time.DateFormatUtils;
import org.omnifaces.util.Messages;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import shimmer.domain.Bug;
import shimmer.domain.Edge;
import shimmer.domain.Graph;
import shimmer.domain.Node;
import shimmer.domain.SimulationProperties;
import shimmer.domain.helper.MetricsHelper;
import shimmer.domain.helper.NamesHelper;
import shimmer.enums.NodeType;
import shimmer.service.GraphService;

/**
 * Standard implementation of methods to operate with Graphs.
 * 
 * @author Filip Daca
 */
@Named
public class GraphServiceImpl implements GraphService {

	// ************************************************************************
	// STATICS
	
	private static final int MINIMAL_LENGTH = 10;
	private static final int MAXIMAL_LENGTH = 200;
	
	// ************************************************************************
	// IMPLEMENTATIONS
	
	@Override
	public String generateEdgesJSON(Graph graph, SimulationProperties properties) {
		try {
			JSONArray edgesArrayJSON = new JSONArray();
			for (Edge edge : graph.getEdges()) {
				// Skipping dependency edges
				if (edge.isDependencyEdge() && !properties.isDependenciesEdges()) {
					continue;
				}
				
				// Skipping package tree edges
				if (edge.isPackageEdge() && !properties.isPackageTreeEdges()) {
					continue;
				}
				
				// Skipping library edges
				if (edge.hasLibrary() && !properties.isLibraryPackages()) {
					continue;
				}
				
				// Skipping directory edges
				if (edge.hasDirectory() && !properties.isDirectoryNodes()) {
					continue;
				}
				
				JSONObject edgeJSON = new JSONObject();
				edgeJSON.put("type", edge.getEdgeType());
				edgeJSON.put("from", edge.getNodeFrom().getId());
				edgeJSON.put("to", edge.getNodeTo().getId());
				if (properties.isDependenciesWeighted()) {
					edgeJSON.put("length", calculateLength(edge));
				}
				edgesArrayJSON.put(edgeJSON);
			}
			return edgesArrayJSON.toString(4);
		} catch (Exception e) {
			e.printStackTrace();
			return "[]";
		}
	}

	@Override
	public String generateNodesJSON(Graph graph, SimulationProperties properties) {
		try {	
			JSONArray nodesArrayJSON = new JSONArray();
			for (Node node : graph.getNodes()) {
				// Skipping library packages
				if (node.isLibraryPackage() && !properties.isLibraryPackages()) {
					continue;
				}
				
				// Skipping directories
				if (node.isDirectory() && !properties.isDirectoryNodes()) {
					continue;
				}
				
				JSONObject nodeJSON = nodeToJSON(node, properties);
				nodesArrayJSON.put(nodeJSON);
			}
			return nodesArrayJSON.toString(4);
		} catch (Exception e) {
			e.printStackTrace();
			return "[]";
		}
	}
	
	@Override
	public JSONObject generateShimmerJSON(String nodesJSON,
			String edgesJSON, SimulationProperties properties) {
		JSONObject shimmerJSON = new JSONObject();
		try {
			shimmerJSON.put("edges", edgesJSON);
			shimmerJSON.put("nodes", nodesJSON);
			shimmerJSON.put("libraryPackages", properties.isLibraryPackages());
			shimmerJSON.put("dependenciesEdges", properties.isDependenciesEdges());
			shimmerJSON.put("dependenciesWeighted", properties.isDependenciesWeighted());
			shimmerJSON.put("directoryNodes", properties.isDirectoryNodes());
			shimmerJSON.put("nodeSizeMetric", properties.getNodeSizeMetric());
			shimmerJSON.put("nodeColorMetric", properties.getNodeColorMetric());
			shimmerJSON.put("nodeHeatMetric", properties.getNodeHeatMetric());
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Unable to prepare simulation JSON.");
		}
		return shimmerJSON;
	}
	
	// ************************************************************************
	// PRIVATE METHODS

	private JSONObject nodeToJSON(Node node, SimulationProperties properties) throws JSONException {
		JSONObject nodeJSON = new JSONObject();
		nodeJSON.put("id", node.getId());
		nodeJSON.put("label", node.getName());
		nodeJSON.put("radius", MetricsHelper.getNodeSize(node, properties.getNodeSizeMetric()));
		nodeJSON.put("color", getNodeColorJSON(node, properties));
		nodeJSON.put("heatValue", MetricsHelper.getNodeHeat(node, properties.getNodeHeatMetric()));
		nodeJSON.put("shape", getNodeShape(node));
		nodeJSON.put("shimmerProperties", getNodeShimmerPropertiesJSON(node));
		return nodeJSON;
	}
	
	private JSONObject getNodeShimmerPropertiesJSON(Node node) throws JSONException {
		JSONObject shimmerPropertiesJSON = new JSONObject();
		shimmerPropertiesJSON.put("nodeType", node.getNodeType());
		shimmerPropertiesJSON.put("nodeTypeText", NamesHelper.enumToNiceString(node.getNodeType()));
		
		if (node.getNodeType() == NodeType.ANALYSED_PACKAGE) {
			shimmerPropertiesJSON.put("classCount", node.getClassCount());
			shimmerPropertiesJSON.put("totalSize", node.getTotalSize());
			shimmerPropertiesJSON.put("averageSize", node.getAverageSize());
			shimmerPropertiesJSON.put("largestClassSize", node.getLargestClassSize());
			shimmerPropertiesJSON.put("concreteClassesCount", node.getConcreteClassesCount());
			shimmerPropertiesJSON.put("abstractClassesCount", node.getAbstractClassesCount());
			shimmerPropertiesJSON.put("abstractness", node.getAbstractness());
			shimmerPropertiesJSON.put("efferentsCount", node.getEfferentsCount());
			shimmerPropertiesJSON.put("afferentsCount", node.getAfferentsCount());
			shimmerPropertiesJSON.put("instability", node.getInstability());
			shimmerPropertiesJSON.put("distanceFromMainSequence", node.getDistanceFromMainSequence());
			shimmerPropertiesJSON.put("totalBugs", node.getTotalBugs());
			shimmerPropertiesJSON.put("bugs", bugsToJSON(node.getBugs()));
			shimmerPropertiesJSON.put("commitsCount", node.getCommitsCount());
			shimmerPropertiesJSON.put("lastCommitDate", DateFormatUtils.format(node.getLastCommitDate(), "dd-MM-yyyy hh:mm"));
			shimmerPropertiesJSON.put("authorsCount", node.getAuthors().size());
			shimmerPropertiesJSON.put("authors", authorsToJSON(node.getAuthors()));
		}
		
		return shimmerPropertiesJSON;
	}

	private JSONArray authorsToJSON(Set<String> authors) {
		JSONArray authorsArrayJSON = new JSONArray();
		for (String author : authors) {
			authorsArrayJSON.put(author);
		}
		return authorsArrayJSON;
	}

	private JSONArray bugsToJSON(List<Bug> bugs) throws JSONException {
		JSONArray bugsArrayJSON = new JSONArray();
		for (Bug bug : bugs) {
			JSONObject bugJSON = new JSONObject();
			bugJSON.put("type", bug.getBugType());
			bugJSON.put("abbrev", bug.getBugAbbrev());
			bugJSON.put("category", bug.getBugCategory());
			bugJSON.put("priority", bug.getBugPriority());
			bugJSON.put("rank", bug.getBugRank());
			bugJSON.put("className", bug.getClassName());
			bugsArrayJSON.put(bugJSON);
		}
		return bugsArrayJSON;
	}

	private JSONObject getNodeColorJSON(Node node, SimulationProperties properties) throws JSONException {
		JSONObject colorJSON = new JSONObject();
		if (properties.getNodeColorMetric() == null) {
			return colorJSON;
		}
		
		if (properties.isConstellation()) {
			colorJSON.put("border", MetricsHelper.CONSTELLATION_COLOR);
		} else {
			colorJSON.put("border", MetricsHelper.GRAPH_COLOR);
		}
		colorJSON.put("background", getNodeColor(node, properties));
		
		return colorJSON;
	}
	
	private String getNodeColor(Node node, SimulationProperties properties) {
		switch (node.getNodeType()) {
		case ANALYSED_PACKAGE:
			return MetricsHelper.getAnalysedPackageColor(node, properties.getNodeColorMetric());

		case LIBRARY_PACKAGE:
			return MetricsHelper.LIBRARY_COLOR;
			
		case DIRECTORY:
			return MetricsHelper.DIRECTORY_COLOR;
			
		default:
			return MetricsHelper.DEFAULT_COLOR;
		}
	}
	
	private String getNodeShape(Node node) {
		if (node.isDirectory()) {
			return MetricsHelper.DIRECTORY_SHAPE;
		} else {
			return MetricsHelper.DEFAULT_SHAPE;
		}
	}

	private int calculateLength(Edge edge) {
		int classCount = edge.getNodeFrom().getClassCount();
		int strength = edge.getStrength();
		int length = MINIMAL_LENGTH;
		
		switch (edge.getEdgeType()) {
		case PACKAGE_EDGE:
			length = MINIMAL_LENGTH;
			break;
			
		case DEPENDENCY_EDGE:
			if (classCount > 0) {
				float weight = (float) strength / (float) classCount;
				length = MAXIMAL_LENGTH - Math.round(weight * MAXIMAL_LENGTH) + MINIMAL_LENGTH; 
			}
			break;
		}
				
		return length;
	}

}
