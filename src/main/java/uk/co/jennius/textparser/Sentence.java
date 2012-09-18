package uk.co.jennius.textparser;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
	private String text;
	private List<Word> words;
	public Sentence(String text){
		this.text = text;
		this.words = new ArrayList<Word>();
	}
	
	public String get_text(){
		return text;
	}

	public List<Word> get_words() {
		return words;
	}

	public void addWord(Word word) {
		words.add(word);
	}	
}
