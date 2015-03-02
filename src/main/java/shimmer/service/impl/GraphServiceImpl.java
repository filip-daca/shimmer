package shimmer.service.impl;

import java.awt.Color;
import java.util.List;

import javax.inject.Named;

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
			if (properties.isDependenciesWeighted()) {
				appendLength(sb, edge);
			}
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
		
		sb.append("nodeType: '");
		sb.append(node.getNodeType());
		sb.append("', ");
		
		sb.append("nodeTypeText: '");
		sb.append(NamesHelper.enumToNiceString(node.getNodeType()));
		sb.append("', ");
		
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
		
		if (node.getNodeType() == NodeType.ANALYSED_PACKAGE) {
			sb.append("bugs: [");
			appendBugs(sb, node.getBugs());
			sb.append("], ");
		}
		
		sb.append("}, ");
	}

	private void appendBugs(StringBuilder sb, List<Bug> bugs) {
		for (Bug bug : bugs) {
			sb.append("{");
			sb.append("type: '");
			sb.append(bug.getBugType());
			sb.append("', abbrev: '");
			sb.append(bug.getBugAbbrev());
			sb.append("', category: '");
			sb.append(bug.getBugCategory());
			sb.append("', priority: ");
			sb.append(bug.getBugPriority());
			sb.append(", rank: ");
			sb.append(bug.getBugRank());
			sb.append("},");
		}
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
		
		if (properties.isConstellation()) {
			sb.append("color: {border: 'transparent', background: ");
		} else {
			sb.append("color: {border: 'black', background: ");
		}
		
		switch (node.getNodeType()) {
		case ANALYSED_PACKAGE:
			appendAnalysedPackageColor(sb, node, properties);
			break;

		case LIBRARY_PACKAGE:
			sb.append("'purple'");
			break;
			
		case TREE_NODE:
			sb.append("'blue'");
			break;
			
		default:
			break;
		}
		
		sb.append("}, ");
	}
	
	private void appendAnalysedPackageColor(StringBuilder sb, Node node,
			SimulationProperties properties) {
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
			sb.append(Math.min((MINIMAL_RADIUS + node.getClassCount() / 1.5), MAXIMAL_RADIUS));
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

	private void appendLength(StringBuilder sb, Edge edge) {
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
				
		sb.append(", length:");
		sb.append(length);
	}

}
