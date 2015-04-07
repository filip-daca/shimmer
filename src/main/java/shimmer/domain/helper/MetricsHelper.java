package shimmer.domain.helper;

import java.awt.Color;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

import shimmer.domain.MetricProperties;
import shimmer.domain.Node;
import shimmer.enums.Metric;
import shimmer.enums.NodeType;

/**
 * Helps counting metrics.
 * @author Filip Daca
 */
public class MetricsHelper {
	
	// ************************************************************************
	// STATIC FIELDS
	
	public static final int MINIMAL_HEAT = 0;
	public static final int MAXIMAL_HEAT = 70;
	
	public static final String DEFAULT_COLOR = "lightgray";
	public static final String LIBRARY_COLOR = "purple";
	public static final String DIRECTORY_COLOR = "blue";
	
	public static final String CONSTELLATION_COLOR = "transparent";
	public static final String GRAPH_COLOR = "black";
	
	public static final String DIRECTORY_SHAPE = "square";
	public static final String DEFAULT_SHAPE = "dot";
	
	// ************************************************************************
	// IMPLEMENTATIONS
	
	/**
	 * Returns node heat.
	 * @param node - shimmer node
	 * @param heatMetric - metric to use
	 * @param properties - calibration properties
	 * @return heat value
	 */
	public static int getNodeHeat(Node node, Metric heatMetric, MetricProperties properties) {
		
		if (heatMetric == null || node.getNodeType() != NodeType.ANALYSED_PACKAGE) {
			return 0;
		}
		
		switch (heatMetric) {
			case DISTANCE_FROM_MAIN_SEQUENCE:
				return metricToHeat(0, 1, node.getDistanceFromMainSequence());
				
			case ABSTRACTNESS:
				return metricToHeat(0, 1, node.getAbstractness());
				
			case INSTABILITY:
				return metricToHeat(0, 1, node.getInstability());
				
			case CLASS_COUNT:
				return metricToHeat(properties.getBigEnoughPackage(), properties.getTooBigPackage(), node.getClassCount());
				
			case AVERAGE_SIZE:
				return metricToHeat(properties.getBigEnoughClass(), properties.getTooBigClass(), node.getAverageSize());
				
			case OFTEN_CHANGED:
				return metricToHeat(0, properties.getManyCommits(), node.getCommitsCount());
				
			case RECENTLY_CHANGED:
				return metricToHeat(properties.getOldCommitDays(), properties.getNewCommitDays(), 
						Days.daysBetween(new DateTime(node.getLastCommitDate()), new DateTime(new Date())).getDays());
				
			default:
				return MINIMAL_HEAT;
		}
	}
	
	public static String getAnalysedPackageColor(Node node, Metric colorMetric, MetricProperties properties) {
		switch (colorMetric) {
			case DISTANCE_FROM_MAIN_SEQUENCE:
				return metricToColor(0, 1, node.getDistanceFromMainSequence());
				
			case ABSTRACTNESS:
				return metricToColor(0, 1, Color.LIGHT_GRAY, Color.CYAN, node.getAbstractness());
				
			case INSTABILITY:
				return metricToColor(0, 1, Color.LIGHT_GRAY, Color.YELLOW, node.getInstability());
				
			case ABSTRACT_OR_UNSTABLE:
				return metricToColor(0, 1, Color.CYAN, Color.LIGHT_GRAY, Color.YELLOW, node.getInstability(), node.getAbstractness());
				
			case CLASS_COUNT:
				return metricToColor(properties.getBigEnoughPackage(), properties.getTooBigPackage(), node.getClassCount());
			
			case AVERAGE_SIZE:
				return metricToColor(properties.getBigEnoughClass(), properties.getTooBigClass(), node.getAverageSize());
				
			case LARGEST_CLASS_SIZE:
				return metricToColor(properties.getBigEnoughClass(), properties.getTooBigClass(), node.getLargestClassSize());
			
			case TOTAL_BUGS:
				return metricToColor(0, properties.getTooManyBugs(), node.getTotalBugs());
			
			default:
				return DEFAULT_COLOR;
			}
	}
	
	public static int getNodeSize(Node node, Metric sizeMetric, MetricProperties metricProperties) {
		if (sizeMetric == null) {
			return 0;
		}
		
		int minimalRadius = metricProperties.getMinimalRadius();
		int maximalRadius = metricProperties.getMaximalRadius();
		
		switch (sizeMetric) {
			
			case CLASS_COUNT:
				return (int) Math.min((minimalRadius + node.getClassCount() / 1.5), maximalRadius);
			
			case AVERAGE_SIZE:
				return (int) Math.min((minimalRadius + node.getAverageSize() / 10), maximalRadius);
				
			case LARGEST_CLASS_SIZE:
				return (int) Math.min((minimalRadius + node.getLargestClassSize() / 10), maximalRadius);
				
			case TOTAL_BUGS:
				return (int) Math.min((minimalRadius + node.getTotalBugs() * 2), maximalRadius);
				
			default:
				return minimalRadius;
		}
	}
	
	// ************************************************************************
	// PRIVATE METHODS

	private static int metricToHeat(float min, float max, float val) {
		if (min > max) {
			val = min + max - val;
			float temp = min;
			min = max;
			max = temp;
		}
		
		if (val <= min) {
			return MINIMAL_HEAT;
		} else if (val >= max) {
			return MAXIMAL_HEAT;
		}	
		
		float percent = (val - min) / (max - min);
		return Math.round(percent * (MAXIMAL_HEAT - MINIMAL_HEAT));
	}
	
	private static String metricToColor(float best, float worst, float value) {
		return metricToColor(best, worst, Color.GREEN, Color.RED, value);
	}
	
	private static String metricToColor(float best, float worst, Color color1,
			Color defaultColor, Color color2, float val1, float val2) {
		if (val1 > val2) {
			return metricToColor(best, worst, defaultColor, color1, (float) Math.sqrt(val1 * val1 - val2 * val2));
		} else {
			return metricToColor(best, worst, defaultColor, color2, (float) Math.sqrt(val2 * val2 - val1 * val1));
		}
	}
	
	private static String metricToColor(float best, float worst, Color goodColor, Color badColor, float value) {
		Color result;
		if (best < worst) {
			result = ColorsHelper.colorTransition(goodColor, badColor, best, worst, value);
		} else {
			result = ColorsHelper.colorTransition(badColor, goodColor, worst, best, value);
		}
		return String.format("#%02x%02x%02x", result.getRed(), 
				result.getGreen(), result.getBlue());
	}
}
