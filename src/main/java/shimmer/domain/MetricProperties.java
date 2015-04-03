package shimmer.domain;

/**
 * Basic properties for metrics calibration.
 * @author filip.hubert.daca@gmail.com
 */
public class MetricProperties {

	// ************************************************************************
	// FIELDS
	
	private int minimalRadius;
	private int maximalRadius;
	private int newCommitDays;
	private int oldCommitDays;
	private int manyCommits;
	private int tooManyBugs;
	private int bigEnoughPackage;
	private int tooBigPackage;
	private int bigEnoughClass;
	private int tooBigClass;
	
	// ************************************************************************
	// CONSTRUCTORS
	
	public MetricProperties() {
		this.minimalRadius = 7;
		this.maximalRadius = 30;
		this.newCommitDays = 7;
		this.oldCommitDays = 30;
		this.manyCommits = 100;
		this.tooManyBugs = 5;
		this.bigEnoughPackage = 5;
		this.tooBigPackage = 50;
		this.bigEnoughClass = 40;
		this.tooBigClass = 200;
	}

	// ************************************************************************
	// GETTERS / SETTERS
	
	public int getMaximalRadius() {
		return maximalRadius;
	}
	
	public void setMaximalRadius(int maximalRadius) {
		this.maximalRadius = maximalRadius;
	}
	
	public int getMinimalRadius() {
		return minimalRadius;
	}
	
	public void setMinimalRadius(int minimalRadius) {
		this.minimalRadius = minimalRadius;
	}
	
	public int getNewCommitDays() {
		return newCommitDays;
	}
	
	public void setNewCommitDays(int newCommitDays) {
		this.newCommitDays = newCommitDays;
	}
	
	public int getOldCommitDays() {
		return oldCommitDays;
	}
	
	public void setOldCommitDays(int oldCommitDays) {
		this.oldCommitDays = oldCommitDays;
	}
	
	public int getManyCommits() {
		return manyCommits;
	}
	
	public void setManyCommits(int manyCommits) {
		this.manyCommits = manyCommits;
	}
	
	public int getTooBigClass() {
		return tooBigClass;
	}
	
	public void setTooBigClass(int tooBigClass) {
		this.tooBigClass = tooBigClass;
	}
	
	public int getTooBigPackage() {
		return tooBigPackage;
	}
	
	public void setTooBigPackage(int tooBigPackage) {
		this.tooBigPackage = tooBigPackage;
	}
	
	public int getTooManyBugs() {
		return tooManyBugs;
	}
	
	public void setTooManyBugs(int tooManyBugs) {
		this.tooManyBugs = tooManyBugs;
	}
	
	public int getBigEnoughClass() {
		return bigEnoughClass;
	}
	
	public void setBigEnoughClass(int bigEnoughClass) {
		this.bigEnoughClass = bigEnoughClass;
	}
	
	public int getBigEnoughPackage() {
		return bigEnoughPackage;
	}
	
	public void setBigEnoughPackage(int bigEnoughPackage) {
		this.bigEnoughPackage = bigEnoughPackage;
	}
}
