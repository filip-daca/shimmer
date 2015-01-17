package shimmer.service.impl;

import java.awt.Color;

import javax.inject.Named;

import shimmer.domain.Edge;
import shimmer.domain.Graph;
import shimmer.domain.Node;
import shimmer.domain.SimulationProperties;
import shimmer.enums.NodeType;
import shimmer.service.GraphService;

/**
 * Standard implementation of methods to operate with Graphs.
 * 
 * @author filip.daca@javatech.com.pl
 */
@Named
public class GraphServiceImpl implements GraphService {

	// ************************************************************************
	// STATICS
	
	private static final int MINIMAL_RADIUS = 7;
	private static final int MAXIMAL_RADIUS = 30;
	
	private static final int MINIMAL_HEAT = 0;
	private static final int MAXIMAL_HEAT = 70;
	
	private static final int TOO_LARGE_PACKAGE = 50;
	private static final float GOOD_METRIC = 0.1F;
	private static final float BAD_METRIC = 0.9F;
	
	// ************************************************************************
	// IMPLEMENTATIONS
	
	@Override
	public String generateEdgesJSON(Graph graph, SimulationProperties properties) {
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (Edge edge : graph.getEdges()) {
			sb.append("{");
			sb.append("from:");
			sb.append(edge.getNodeFrom().getId());
			sb.append(", to:");
			sb.append(edge.getNodeTo().getId());
			edge.getNodeFrom();
			sb.append("}, \n");
		}
		sb.append("]\n");
		return sb.toString();
	}

	@Override
	public String generateNodesJSON(Graph graph, SimulationProperties properties) {
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (Node node : graph.getNodes()) {
			appendNode(sb, node, properties);
		}
		sb.append("]\n");
		return sb.toString();
	}
	
	// ************************************************************************
	// PRIVATE METHODS

	private void appendNode(StringBuilder sb, Node node, SimulationProperties properties) {
		sb.append("{");
		appendId(sb, node);
		appendSize(sb, node, properties);
		appendColor(sb, node, properties);
		appendHeat(sb, node, properties);
		appendName(sb, node);
		appendShape(sb, node);
		appendProperties(sb, node);
		sb.append("}, \n");
	}
	
	private void appendProperties(StringBuilder sb, Node node) {
		sb.append("shimmerProperties: {");
		
		sb.append("classCount: ");
		sb.append(node.getClassCount());
		sb.append(", ");
		
		sb.append("concreteClassesCount: ");
		sb.append(node.getConcreteClassesCount());
		sb.append(", ");
		
		sb.append("abstractClassesCount: ");
		sb.append(node.getAbstractClassesCount());
		sb.append(", ");
		
		sb.append("abstractness: ");
		sb.append(node.getAbstractness());
		sb.append(", ");
		
		sb.append("efferentsCount: ");
		sb.append(node.getEfferentsCount());
		sb.append(", ");
		
		sb.append("afferentsCount: ");
		sb.append(node.getAfferentsCount());
		sb.append(", ");
		
		sb.append("instability: ");
		sb.append(node.getInstability());
		sb.append(", ");
		
		sb.append("distanceFromMainSequence: ");
		sb.append(node.getDistanceFromMainSequence());
		sb.append(", ");
		
		sb.append("}, ");
	}

	private void appendId(StringBuilder sb, Node node) {
		sb.append("id:");
		sb.append(node.getId());
		sb.append(", ");
	}

	private void appendName(StringBuilder sb, Node node) {
		sb.append("label: '");
		sb.append(node.getName());
		sb.append("', ");
	}

	private void appendHeat(StringBuilder sb, Node node,
			SimulationProperties properties) {
		if (properties.getNodeHeatMetric() == null) {
			return;
		}
		
		sb.append("heatValue: ");
		
		switch (properties.getNodeHeatMetric()) {
		case DISTANCE_FROM_MAIN_SEQUENCE:
			sb.append(floatMetricToNumber(node.getDistanceFromMainSequence(), MINIMAL_HEAT, MAXIMAL_HEAT));
			break;
			
		case ABSTRACTNESS:
			sb.append(floatMetricToNumber(node.getAbstractness(), MINIMAL_HEAT, MAXIMAL_HEAT));
			break;
			
		case INSTABILITY:
			sb.append(floatMetricToNumber(node.getInstability(), MINIMAL_HEAT, MAXIMAL_HEAT));
			break;
			
		case CLASS_COUNT:
			sb.append(node.getClassCount());
			break;
		}
		sb.append(", ");
	}

	private void appendColor(StringBuilder sb, Node node,
			SimulationProperties properties) {
		if (properties.getNodeColorMetric() == null) {
			return;
		}
		
		sb.append("color: {border: 'transparent', background: ");
		switch (properties.getNodeColorMetric()) {
		case DISTANCE_FROM_MAIN_SEQUENCE:
			sb.append(floatMetricToColor(node.getDistanceFromMainSequence()));
			break;
			
		case ABSTRACTNESS:
			sb.append(floatMetricToColor(node.getAbstractness()));
			break;
			
		case INSTABILITY:
			sb.append(floatMetricToColor(node.getInstability()));
			break;
			
		case CLASS_COUNT:
			// FIXME: find a better way to represent a color
			sb.append(floatMetricToColor((float) node.getClassCount() / (float) TOO_LARGE_PACKAGE));
			break;
		}
		sb.append("}, ");
	}

	private void appendSize(StringBuilder sb, Node node,
			SimulationProperties properties) {
		if (properties.getNodeSizeMetric() == null) {
			return;
		}
		
		sb.append("radius: ");
		switch (properties.getNodeSizeMetric()) {
		case DISTANCE_FROM_MAIN_SEQUENCE:
			sb.append(floatMetricToNumber(node.getDistanceFromMainSequence(), MINIMAL_RADIUS, MAXIMAL_RADIUS));
			break;
			
		case ABSTRACTNESS:
			sb.append(floatMetricToNumber(node.getAbstractness(), MINIMAL_RADIUS, MAXIMAL_RADIUS));
			break;
			
		case INSTABILITY:
			sb.append(floatMetricToNumber(node.getInstability(), MINIMAL_RADIUS, MAXIMAL_RADIUS));
			break;
			
		case CLASS_COUNT:
			sb.append(MINIMAL_RADIUS + node.getClassCount() / 1.5);
			break;
		}
		sb.append(", ");
	}
	
	private void appendShape(StringBuilder sb, Node node) {
		sb.append("shape: ");
		if (node.getNodeType() == NodeType.TREE_NODE) {
			sb.append("'square'");
		} else {
			sb.append("'dot'");
		}
		sb.append(", ");
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
		return String.format("'#%02x%02x%02x'", result.getRed(), 
				result.getGreen(), result.getBlue());
	}

}
