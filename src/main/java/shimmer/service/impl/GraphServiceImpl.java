package shimmer.service.impl;

import java.awt.Color;
import java.util.List;

import javax.inject.Named;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import shimmer.domain.Bug;
import shimmer.domain.Edge;
import shimmer.domain.Graph;
import shimmer.domain.Node;
import shimmer.domain.SimulationProperties;
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
	
	private static final int MINIMAL_RADIUS = 7;
	private static final int MAXIMAL_RADIUS = 30;
	
	private static final int MINIMAL_HEAT = 0;
	private static final int MAXIMAL_HEAT = 70;
	
	private static final int TOO_LARGE_PACKAGE = 50;
	private static final int TOO_LARGE_CLASS = 500;
	private static final float GOOD_METRIC = 0.1F;
	private static final float BAD_METRIC = 0.9F;
	
	// ************************************************************************
	// IMPLEMENTATIONS
	
	@Override
	public String generateEdgesJSON(Graph graph, SimulationProperties properties) {
		try {
			JSONArray edgesArrayJSON = new JSONArray();
			for (Edge edge : graph.getEdges()) {
				JSONObject edgeJSON = new JSONObject();
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
				JSONObject nodeJSON = nodeToJSON(node, properties);
				nodesArrayJSON.put(nodeJSON);
			}
			return nodesArrayJSON.toString(4);
		} catch (Exception e) {
			e.printStackTrace();
			return "[]";
		}
	}
	
	// ************************************************************************
	// PRIVATE METHODS

	private JSONObject nodeToJSON(Node node, SimulationProperties properties) throws JSONException {
		JSONObject nodeJSON = new JSONObject();
		nodeJSON.put("id", node.getId());
		nodeJSON.put("label", node.getName());
		nodeJSON.put("radius", getNodeSize(node, properties));
		nodeJSON.put("color", getNodeColorJSON(node, properties));
		nodeJSON.put("heatValue", getNodeHeat(node, properties));
		nodeJSON.put("shape", getNodeShape(node));
		nodeJSON.put("shimmerProperties", getNodeShimmerPropertiesJSON(node));
		return nodeJSON;
	}
	
	private JSONObject getNodeShimmerPropertiesJSON(Node node) throws JSONException {
		JSONObject shimmerPropertiesJSON = new JSONObject();
		shimmerPropertiesJSON.put("nodeType", node.getNodeType());
		shimmerPropertiesJSON.put("nodeTypeText", NamesHelper.enumToNiceString(node.getNodeType()));
		shimmerPropertiesJSON.put("classCount", node.getClassCount());
		shimmerPropertiesJSON.put("totalSize", node.getTotalSize());
		shimmerPropertiesJSON.put("averageSize", node.getAverageSize());
		shimmerPropertiesJSON.put("concreteClassesCount", node.getConcreteClassesCount());
		shimmerPropertiesJSON.put("abstractClassesCount", node.getAbstractClassesCount());
		shimmerPropertiesJSON.put("abstractness", node.getAbstractness());
		shimmerPropertiesJSON.put("efferentsCount", node.getEfferentsCount());
		shimmerPropertiesJSON.put("afferentsCount", node.getAfferentsCount());
		shimmerPropertiesJSON.put("instability", node.getInstability());
		shimmerPropertiesJSON.put("distanceFromMainSequence", node.getDistanceFromMainSequence());
		shimmerPropertiesJSON.put("totalBugs", node.getTotalBugs());
		
		if (node.getNodeType() == NodeType.ANALYSED_PACKAGE) {
			shimmerPropertiesJSON.put("bugs", bugsToJSON(node.getBugs()));
		}
		
		return shimmerPropertiesJSON;
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
			bugsArrayJSON.put(bugJSON);
		}
		return bugsArrayJSON;
	}

	private int getNodeHeat(Node node, SimulationProperties properties) {
		if (properties.getNodeHeatMetric() == null) {
			return 0;
		}
		switch (properties.getNodeHeatMetric()) {
		case DISTANCE_FROM_MAIN_SEQUENCE:
			return floatMetricToNumber(node.getDistanceFromMainSequence(), MINIMAL_HEAT, MAXIMAL_HEAT);
			
		case ABSTRACTNESS:
			return floatMetricToNumber(node.getAbstractness(), MINIMAL_HEAT, MAXIMAL_HEAT);
			
		case INSTABILITY:
			return floatMetricToNumber(node.getInstability(), MINIMAL_HEAT, MAXIMAL_HEAT);
			
		case CLASS_COUNT:
			return node.getClassCount();
			
		case AVERAGE_SIZE:
			return floatMetricToNumber(node.getAverageSize(), MINIMAL_HEAT, MAXIMAL_HEAT);
			
		default:
			return 0;
		}
	}

	private JSONObject getNodeColorJSON(Node node, SimulationProperties properties) throws JSONException {
		JSONObject colorJSON = new JSONObject();
		if (properties.getNodeColorMetric() == null) {
			return colorJSON;
		}
		
		if (properties.isConstellation()) {
			colorJSON.put("border", "transparent");
		} else {
			colorJSON.put("border", "black");
		}
		colorJSON.put("background", getNodeColor(node, properties));
		
		return colorJSON;
	}
	
	private String getNodeColor(Node node, SimulationProperties properties) {
		switch (node.getNodeType()) {
		case ANALYSED_PACKAGE:
			return getAnalysedPackageColor(node, properties);

		case LIBRARY_PACKAGE:
			return "purple";
			
		case TREE_NODE:
			return "blue";
			
		default:
			return "lightgray";
		}
	}

	private String getAnalysedPackageColor(Node node, SimulationProperties properties) {
		switch (properties.getNodeColorMetric()) {
		case DISTANCE_FROM_MAIN_SEQUENCE:
			return floatMetricToColor(node.getDistanceFromMainSequence());
			
		case ABSTRACTNESS:
			return floatMetricToColor(node.getAbstractness());
			
		case INSTABILITY:
			return floatMetricToColor(node.getInstability());
			
		case CLASS_COUNT:
			// FIXME: find a better way to represent a color
			return floatMetricToColor((float) node.getClassCount() / (float) TOO_LARGE_PACKAGE);
		
		case AVERAGE_SIZE:
			// FIXME: find a better way to represent a color
			return floatMetricToColor((float) node.getAverageSize() / (float) TOO_LARGE_CLASS);
			
		default:
			return "lightgray";
		}
	}

	private int getNodeSize(Node node, SimulationProperties properties) {
		if (properties.getNodeSizeMetric() == null) {
			return 0;
		}
		
		switch (properties.getNodeSizeMetric()) {
		case DISTANCE_FROM_MAIN_SEQUENCE:
			return floatMetricToNumber(node.getDistanceFromMainSequence(), MINIMAL_RADIUS, MAXIMAL_RADIUS);
			
		case ABSTRACTNESS:
			return floatMetricToNumber(node.getAbstractness(), MINIMAL_RADIUS, MAXIMAL_RADIUS);
			
		case INSTABILITY:
			return floatMetricToNumber(node.getInstability(), MINIMAL_RADIUS, MAXIMAL_RADIUS);
			
		case CLASS_COUNT:
			return (int) Math.min((MINIMAL_RADIUS + node.getClassCount() / 1.5), MAXIMAL_RADIUS);
		
		case AVERAGE_SIZE:
			return (int) Math.min((MINIMAL_RADIUS + node.getAverageSize() / 50), MAXIMAL_RADIUS);
			
		default:
			return MINIMAL_RADIUS;
		}
	}
	
	private String getNodeShape(Node node) {
		if (node.getNodeType() == NodeType.TREE_NODE) {
			return "square";
		} else {
			return "dot";
		}
	}
	
	private int floatMetricToNumber(float metric, int min, int max) {
		metric = min + metric * (max - min);
		return Math.round(metric);
	}
	
	private String floatMetricToColor(float metric) {
		Color result;
		if (metric > BAD_METRIC) {
			result = Color.RED;
		} else if (metric > GOOD_METRIC) {
			result = Color.LIGHT_GRAY;
		} else {
			result = Color.GREEN;
		}
		return String.format("#%02x%02x%02x", result.getRed(), 
				result.getGreen(), result.getBlue());
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
