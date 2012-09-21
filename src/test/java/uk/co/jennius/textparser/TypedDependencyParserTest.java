package uk.co.jennius.textparser;

import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TypedDependencyParserTest {

	private TypedDependencyParser parser(TypedDependencyOptions option) {
		return new TypedDependencyParser(option);
	}

	@Test
	public void getTypedDependencies_ShouldSplitSentences()
			throws TextParserException {
		List<Sentence> sentences = parser(
				TypedDependencyOptions.OnlyDependencies).getTypedDependencies(
				"I'm a programmer. I go home now");
		assertThat(sentences.size(), is(2));
	}

	@Test
	public void getTypedDependenciesWithOnlyDependencies_ShouldReturnDependenciesInSentence()
			throws TextParserException {
		List<Sentence> sentences = parser(
				TypedDependencyOptions.OnlyDependencies).getTypedDependencies(
				"I'm a programmer!");
		assertThat(sentences.size(), is(1));
		Sentence sentence = sentences.get(0);
		assertThat(sentence.getAtoms().size(), is(4));
		Dependency iWord = (Dependency) sentence.getAtoms().get(0);
		Dependency amWord = (Dependency) sentence.getAtoms().get(1);
		Dependency aWord = (Dependency) sentence.getAtoms().get(2);
		Dependency programmerWord = (Dependency) sentence.getAtoms()
				.get(3);

		assertThat(iWord.getText(), is("I"));
		assertThat(iWord.getGov(), is("programmer"));
		assertThat(iWord.getIndex(), is(0));
		assertThat(iWord.getGovIndex(), is(3));
		assertThat(iWord.getRelation(), is("nsubj"));

		assertThat(amWord.getText(), is("'m"));
		assertThat(amWord.getGov(), is("programmer"));
		assertThat(amWord.getIndex(), is(1));
		assertThat(amWord.getGovIndex(), is(3));
		assertThat(amWord.getRelation(), is("cop"));

		assertThat(aWord.getText(), is("a"));
		assertThat(aWord.getGov(), is("programmer"));
		assertThat(aWord.getIndex(), is(2));
		assertThat(aWord.getGovIndex(), is(3));
		assertThat(aWord.getRelation(), is("det"));

		assertThat(programmerWord.getText(), is("programmer"));
		assertThat(programmerWord.getGov(), is(""));
		assertThat(programmerWord.getIndex(), is(3));
		assertThat(programmerWord.getGovIndex(), is(-1));
		assertThat(programmerWord.getRelation(), is("root"));
	}

	@Test
	public void getTypedDependenciesPreservingStructure_ShouldReturnDependenciesAndFillingsInSentence()
			throws TextParserException {
		List<Sentence> sentences = parser(
				TypedDependencyOptions.PreserveSentenceStructure)
				.getTypedDependencies(" I'm a  programmer! ");
		assertThat(sentences.size(), is(1));
		Sentence sentence = sentences.get(0);
		assertThat(sentence.getAtoms().size(), is(7));

		Dependency iWord = (Dependency) sentence.getAtoms().get(0);
		Dependency amWord = (Dependency) sentence.getAtoms().get(1);
		Filling space2 = (Filling) sentence.getAtoms().get(2);
		Dependency aWord = (Dependency) sentence.getAtoms().get(3);
		Filling space3 = (Filling) sentence.getAtoms().get(4);
		Dependency programmerWord = (Dependency) sentence.getAtoms().get(5);
		Filling exclamation = (Filling) sentence.getAtoms().get(6);

		assertThat(iWord.getText(), is("I"));
		assertThat(iWord.getGov(), is("programmer"));
		assertThat(iWord.getIndex(), is(0));
		assertThat(iWord.getGovIndex(), is(5));
		assertThat(iWord.getRelation(), is("nsubj"));
		
		assertThat(amWord.getText(), is("'m"));
		assertThat(amWord.getGov(), is("programmer"));
		assertThat(amWord.getIndex(), is(1));
		assertThat(amWord.getGovIndex(), is(5));
		assertThat(amWord.getRelation(), is("cop"));
		
		assertThat(space2.getText(), is(" "));
		assertThat(space2.getIndex(), is(2));
		
		assertThat(aWord.getText(), is("a"));
		assertThat(aWord.getGov(), is("programmer"));
		assertThat(aWord.getIndex(), is(3));
		assertThat(aWord.getGovIndex(), is(5));
		assertThat(aWord.getRelation(), is("det"));
		
		assertThat(space3.getText(), is("  "));
		assertThat(space3.getIndex(), is(4));
		
		assertThat(programmerWord.getText(), is("programmer"));
		assertThat(programmerWord.getGov(), is(""));
		assertThat(programmerWord.getIndex(), is(5));
		assertThat(programmerWord.getGovIndex(), is(-1));
		assertThat(programmerWord.getRelation(), is("root"));
		
		assertThat(exclamation.getText(), is("!"));
		assertThat(exclamation.getIndex(), is(6));	
	}
}
