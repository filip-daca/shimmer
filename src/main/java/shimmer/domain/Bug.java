package shimmer.domain;

public class Bug {

	private String bugType;
	private String bugCategory;
	private String bugAbbrev;
	private int bugRank;
	private int bugPriority;
	
	public Bug(String bugType, String bugCategory, String bugAbbrev,
			int bugRank, int bugPriority) {
		this.bugType = bugType;
		this.bugCategory = bugCategory;
		this.bugAbbrev = bugAbbrev;
		this.bugRank = bugRank;
		this.bugPriority = bugPriority;
	}

	public String getBugType() {
		return bugType;
	}
	
	public String getBugCategory() {
		return bugCategory;
	}
	
	public String getBugAbbrev() {
		return bugAbbrev;
	}
	
	public int getBugRank() {
		return bugRank;
	}
	
	public int getBugPriority() {
		return bugPriority;
	}
	
}
