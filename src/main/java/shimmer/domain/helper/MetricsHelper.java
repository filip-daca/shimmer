package shimmer.domain.helper;

import java.awt.Color;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

import shimmer.domain.Node;
import shimmer.domain.SimulationProperties;
import shimmer.enums.Metric;
import shimmer.enums.NodeType;

/**
 * Helps counting metrics.
 * @author filip.daca@javatech.com.pl
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
	
	public static final int MINIMAL_RADIUS = 7;
	public static final int MAXIMAL_RADIUS = 30;
	
	public static final int BIG_ENOUGH_PACKAGE = 5;
	public static final int TOO_LARGE_PACKAGE = 50;
	
	public static final int BIG_ENOUGH_CLASS = 40;
	public static final int TOO_LARGE_CLASS = 200;
	
	public static final int TOO_MANY_BUGS = 5;
	
	public static final int MANY_COMMITS = 100;
	
	public static final int OLD_COMMIT_DAYS = 30;
	public static final int NEW_COMMIT_DAYS = 7;
	
	// ************************************************************************
	// IMPLEMENTATIONS
	
	/**
	 * Returns node heat.
	 * @param node - shimmer node
	 * @param heatMetric - metric to use
	 * @return heat value
	 */
	public static int getNodeHeat(Node node, Metric heatMetric) {
		
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
				return metricToHeat(BIG_ENOUGH_PACKAGE, TOO_LARGE_PACKAGE, node.getClassCount());
				
			case AVERAGE_SIZE:
				return metricToHeat(BIG_ENOUGH_CLASS, TOO_LARGE_CLASS, node.getAverageSize());
				
			case OFTEN_CHANGED:
				return metricToHeat(0, MANY_COMMITS, node.getCommitsCount());
				
			case RECENTLY_CHANGED:
				return metricToHeat(OLD_COMMIT_DAYS, NEW_COMMIT_DAYS, 
						Days.daysBetween(new DateTime(node.getLastCommitDate()), new DateTime(new Date())).getDays());
				
			default:
				return MINIMAL_HEAT;
		}
	}
	
	public static String getAnalysedPackageColor(Node node, Metric colorMetric) {
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
				return metricToColor(BIG_ENOUGH_PACKAGE, TOO_LARGE_PACKAGE, node.getClassCount());
			
			case AVERAGE_SIZE:
				return metricToColor(BIG_ENOUGH_CLASS, TOO_LARGE_CLASS, node.getAverageSize());
				
			case LARGEST_CLASS_SIZE:
				return metricToColor(BIG_ENOUGH_CLASS, TOO_LARGE_CLASS, node.getLargestClassSize());
			
			case TOTAL_BUGS:
				return metricToColor(0, TOO_MANY_BUGS, node.getTotalBugs());
			
			default:
				return DEFAULT_COLOR;
			}
	}
	
	public static int getNodeSize(Node node, Metric sizeMetric) {
		if (sizeMetric == null) {
			return 0;
		}
		
		switch (sizeMetric) {
			
			case CLASS_COUNT:
				return (int) Math.min((MINIMAL_RADIUS + node.getClassCount() / 1.5), MAXIMAL_RADIUS);
			
			case AVERAGE_SIZE:
				return (int) Math.min((MINIMAL_RADIUS + node.getAverageSize() / 10), MAXIMAL_RADIUS);
				
			case LARGEST_CLASS_SIZE:
				return (int) Math.min((MINIMAL_RADIUS + node.getLargestClassSize() / 10), MAXIMAL_RADIUS);
				
			case TOTAL_BUGS:
				return (int) Math.min((MINIMAL_RADIUS + node.getTotalBugs() * 2), MAXIMAL_RADIUS);
				
			default:
				return MINIMAL_RADIUS;
		}
	}
	
	// ************************************************************************
	// PRIVATE METHODS

	private static int metricToHeat(float min, float max, float val) {
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
			return metricToColor(best, worst, defaultColor, color1, val1 - val2);
		} else {
			return metricToColor(best, worst, defaultColor, color2, val2 - val1);
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
