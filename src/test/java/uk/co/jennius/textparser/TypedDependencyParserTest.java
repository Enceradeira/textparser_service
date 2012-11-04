package uk.co.jennius.textparser;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
		// I
		Word iWord = (Word) sentence.getAtoms().get(0);
		assertThat(iWord.getDependencies().size(), is(1));
		Dependency iDependency = iWord.getDependencies().get(0);
		// am
		Word amWord = (Word) sentence.getAtoms().get(1);
		assertThat(amWord.getDependencies().size(), is(1));
		Dependency amDependency = amWord.getDependencies().get(0);
		// a
		Word aWord = (Word) sentence.getAtoms().get(2);
		assertThat(aWord.getDependencies().size(), is(1));
		Dependency aDependency = aWord.getDependencies().get(0);
		// programmer
		Word programmerWord = (Word) sentence.getAtoms().get(3);
		assertThat(programmerWord.getDependencies().size(), is(1));
		Dependency programmerDependency = programmerWord.getDependencies().get(
				0);

		assertThat(iWord.getText(), is("I"));
		assertThat(iWord.getIndex(), is(0));
		assertThat(iDependency.getGov(), is("programmer"));
		assertThat(iDependency.getGovIndex(), is(3));
		assertThat(iDependency.getRelation(), is("nsubj"));

		assertThat(amWord.getText(), is("'m"));
		assertThat(amWord.getIndex(), is(1));
		assertThat(amDependency.getGov(), is("programmer"));
		assertThat(amDependency.getGovIndex(), is(3));
		assertThat(amDependency.getRelation(), is("cop"));

		assertThat(aWord.getText(), is("a"));
		assertThat(aWord.getIndex(), is(2));
		assertThat(aDependency.getGov(), is("programmer"));
		assertThat(aDependency.getGovIndex(), is(3));
		assertThat(aDependency.getRelation(), is("det"));

		assertThat(programmerWord.getText(), is("programmer"));
		assertThat(programmerWord.getIndex(), is(3));
		assertThat(programmerDependency.getGov(), is(""));
		assertThat(programmerDependency.getGovIndex(), is(-1));
		assertThat(programmerDependency.getRelation(), is("root"));
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

		// I
		Word iWord = (Word) sentence.getAtoms().get(0);
		assertThat(iWord.getDependencies().size(), is(1));
		Dependency iDependency = iWord.getDependencies().get(0);
		// 'm
		Word amWord = (Word) sentence.getAtoms().get(1);
		assertThat(amWord.getDependencies().size(), is(1));
		Dependency amDependency = amWord.getDependencies().get(0);
		// ' '
		Filling space2 = (Filling) sentence.getAtoms().get(2);
		// a
		Word aWord = (Word) sentence.getAtoms().get(3);
		assertThat(aWord.getDependencies().size(), is(1));
		Dependency aDependency = aWord.getDependencies().get(0);
		// ' '
		Filling space3 = (Filling) sentence.getAtoms().get(4);
		// programmer
		Word programmerWord = (Word) sentence.getAtoms().get(5);
		assertThat(programmerWord.getDependencies().size(), is(1));
		Dependency programmerDependency = programmerWord.getDependencies().get(
				0);
		// !
		Filling exclamation = (Filling) sentence.getAtoms().get(6);

		assertThat(iWord.getText(), is("I"));
		assertThat(iWord.getIndex(), is(0));
		assertThat(iDependency.getGov(), is("programmer"));
		assertThat(iDependency.getGovIndex(), is(5));
		assertThat(iDependency.getRelation(), is("nsubj"));

		assertThat(amWord.getText(), is("'m"));
		assertThat(amWord.getIndex(), is(1));
		assertThat(amDependency.getGov(), is("programmer"));
		assertThat(amDependency.getGovIndex(), is(5));
		assertThat(amDependency.getRelation(), is("cop"));

		assertThat(space2.getText(), is(" "));
		assertThat(space2.getIndex(), is(2));

		assertThat(aWord.getText(), is("a"));
		assertThat(aWord.getIndex(), is(3));
		assertThat(aDependency.getGov(), is("programmer"));
		assertThat(aDependency.getGovIndex(), is(5));
		assertThat(aDependency.getRelation(), is("det"));

		assertThat(space3.getText(), is("  "));
		assertThat(space3.getIndex(), is(4));

		assertThat(programmerWord.getText(), is("programmer"));
		assertThat(programmerWord.getIndex(), is(5));
		assertThat(programmerDependency.getGov(), is(""));
		assertThat(programmerDependency.getGovIndex(), is(-1));
		assertThat(programmerDependency.getRelation(), is("root"));

		assertThat(exclamation.getText(), is("!"));
		assertThat(exclamation.getIndex(), is(6));
	}

	@Test
	public void getTypedDependencies_ShouldHandleWordThatHaveMoreThanOneGovernor()
			throws TextParserException {
		List<Sentence> sentences = parser(
				TypedDependencyOptions.PreserveSentenceStructure)
				.getTypedDependencies("A cow, which has eyes, is eating.");

		assertThat(sentences.size(), is(1));
		Sentence sentence = sentences.get(0);
				
		Word cowWord = sentence.getWords().get(1);
		
		// Word
		assertThat(cowWord.getText(), is("cow"));
		assertThat(cowWord.getDependencies().size(), is(2));
		Dependency hasDependency = cowWord.getDependencies().get(0);
		Dependency eatingDependency = cowWord.getDependencies().get(1);
		// Dependencies
		assertThat(hasDependency.getGov(), is("has"));
		assertThat(hasDependency.getGovIndex(), is(6));
		assertThat(hasDependency.getRelation(), is("nsubj"));
		assertThat(eatingDependency.getGov(), is("eating"));
		assertThat(eatingDependency.getGovIndex(), is(13));
		assertThat(eatingDependency.getRelation(), is("nsubj"));
			
	}
}
