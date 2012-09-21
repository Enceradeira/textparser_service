package uk.co.jennius.textparser;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
	private List<Atom> atoms;

	public Sentence() {
		this.atoms = new ArrayList<Atom>();
	}

	public List<Atom> getAtoms() {
		return atoms;
	}

	public void add(Dependency word) {
		atoms.add(word);
	}

	public void add(Filling filling) {
		atoms.add(filling);
	}
	
	public boolean isEmpty(){
		return atoms.size() == 0;
	}
	
	public List<Dependency> getDependencies(){
		List<Dependency> result = new ArrayList<Dependency>();
		for(Atom atom: atoms){
			if(atom instanceof Dependency){
				result.add((Dependency) atom);	
			}			
		}
		return result;
	}
}
