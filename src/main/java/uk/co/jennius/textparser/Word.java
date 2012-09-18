package uk.co.jennius.textparser;

public class Word {
	private String dep;
	private String gov;
	private int depIndex;
	private int govIndex;
	private String relation;

	public Word(String dep, String gov, int depIndex, int govIndex, String relation){
		this.dep = dep;
		this.gov = gov;
		this.depIndex = depIndex;
		this.govIndex = govIndex;
		this.relation = relation; 
	}
	
	public String getDep(){
		return dep;
	}
	
	public String getGov(){
		return gov;
	}
	
	public int getDepIndex(){
		return depIndex;
	}
	
	public int getGovIndex(){
		return govIndex;
	}
	
	public String getRelation(){
		return relation;
	}	
}
