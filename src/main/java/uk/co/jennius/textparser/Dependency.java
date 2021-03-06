package uk.co.jennius.textparser;

public class Dependency {

	private String gov;
	private int govIndex;
	private String relation;

	public Dependency(String gov, int govIndex, String relation) {

		this.gov = gov;
		this.govIndex = govIndex;
		this.relation = relation;
	}

	public String getGov() {
		return gov;
	}

	public int getGovIndex() {
		return govIndex;
	}

	public void setGovIndex(int govIndex) {
		this.govIndex = govIndex;
	}

	public String getRelation() {
		return relation;
	}
}
