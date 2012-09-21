package uk.co.jennius.textparser;

public class Atom {
	private String text;
	private int index;

	public Atom(String text, int index) {
		this.text = text;
		this.index = index;
	}

	public String getText() {
		return text;
	}
	
	public int getIndex(){
		return index;
	}
}
