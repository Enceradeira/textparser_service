package uk.co.jennius.textparser;

import java.util.ArrayList;
import java.util.List;

public class Word extends Atom {
	private ArrayList<Dependency> dependencies;

	public Word(String text, int index) {
		super(text, index);
		dependencies = new ArrayList<Dependency>();

	}

	public List<Dependency> getDependencies() {
		return dependencies;
	}

	public void add(Dependency dependency) {
		dependencies.add(dependency);
	}
}
