package shimmer.enums;

/**
 * This enum names a metric to represent in Simulation.
 * 
 * @author Filip Daca
 */
public enum Metric {

	CLASS_COUNT,
	AVERAGE_SIZE,
	LARGEST_CLASS_SIZE,
	ABSTRACTNESS,
	INSTABILITY,
	ABSTRACT_OR_UNSTABLE,
	DISTANCE_FROM_MAIN_SEQUENCE,
	TOTAL_BUGS,
	OFTEN_CHANGED,
	RECENTLY_CHANGED;

	public static Enum<?>[] sizeMetrics() {
		return new Metric[]{CLASS_COUNT, LARGEST_CLASS_SIZE, AVERAGE_SIZE, TOTAL_BUGS};
	}
	
	public static Enum<?>[] colorMetrics() {
		return new Metric[]{CLASS_COUNT, LARGEST_CLASS_SIZE, AVERAGE_SIZE, ABSTRACTNESS,
				INSTABILITY, ABSTRACT_OR_UNSTABLE, DISTANCE_FROM_MAIN_SEQUENCE, TOTAL_BUGS};
	}
	
	public static Enum<?>[] heatMetrics() {
		return new Metric[]{ABSTRACTNESS, INSTABILITY, DISTANCE_FROM_MAIN_SEQUENCE, TOTAL_BUGS,
				OFTEN_CHANGED, RECENTLY_CHANGED};
	}
}
