package shimmer.domain;

public class Bug {

	private String bugType;
	private String bugCategory;
	private String bugAbbrev;
	private int bugRank;
	private int bugPriority;
	
	// TODO: this field is temporary
	private String className;
	
	public Bug(String bugType, String bugCategory, String bugAbbrev,
			int bugRank, int bugPriority, String className) {
		this.bugType = bugType;
		this.bugCategory = bugCategory;
		this.bugAbbrev = bugAbbrev;
		this.bugRank = bugRank;
		this.bugPriority = bugPriority;
		this.className = className;
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
	
	public String getClassName() {
		return className;
	}
	
}
