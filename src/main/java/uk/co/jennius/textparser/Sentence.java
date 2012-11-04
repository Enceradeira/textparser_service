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

	public void add(Word word) {
		atoms.add(word);
	}

	public void add(Filling filling) {
		atoms.add(filling);
	}
	
	public boolean isEmpty(){
		return atoms.size() == 0;
	}
	
	public List<Word> getWords(){
		List<Word> result = new ArrayList<Word>();
		for(Atom atom: atoms){
			if(atom instanceof Word){
				result.add((Word) atom);	
			}			
		}
		return result;
	}
}
