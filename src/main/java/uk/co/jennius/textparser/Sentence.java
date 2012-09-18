package uk.co.jennius.textparser;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
	private String text;
	private List<Dependency> dependencies;
	public Sentence(String text){
		this.text = text;
		this.dependencies = new ArrayList<Dependency>();
	}
	
	public String get_text(){
		return text;
	}

	public List<Dependency> get_dependencies() {
		return dependencies;
	}

	public void addWord(Dependency word) {
		dependencies.add(word);
	}	
}
