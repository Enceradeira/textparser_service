package uk.co.jennius.textparser;


import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


public class TypedDependencyParserTest 
{
	
	private TypedDependencyParser parser;


	@Before
	public void setUp(){
		parser = new TypedDependencyParser();
	}
	
	
	@Test 
	public void getTypedDependencies_ShouldSplitSentences() throws TextParserException
    {
        List<Sentence> sentences = parser.getTypedDependencies("I'm a programmer. I go home now");
        assertThat(sentences.size(), is(2));   
        assertThat(sentences.get(0).get_text(), is("I'm a programmer."));
        assertThat(sentences.get(1).get_text(), is("I go home now"));
    }
	
	@Test
	public void getTypedDependencies_ShouldReturnDependenciesInSentence() throws TextParserException
	{
		List<Sentence> sentences = parser.getTypedDependencies("I'm a programmer!");
		assertThat(sentences.size(), is(1));
		Sentence sentence = sentences.get(0);
		assertThat(sentence.get_dependencies().size(), is(4));
		Dependency iWord = sentence.get_dependencies().get(0);
		Dependency amWord = sentence.get_dependencies().get(1); 
		Dependency aWord = sentence.get_dependencies().get(2); 
		Dependency programmerWord = sentence.get_dependencies().get(3); 
		
		assertThat(iWord.getDep(), is("I"));
		assertThat(iWord.getGov(), is("programmer"));
		assertThat(iWord.getDepIndex(), is(1));
		assertThat(iWord.getGovIndex(), is(4));
		assertThat(iWord.getRelation(), is("nsubj"));
		
		assertThat(amWord.getDep(), is("'m"));
		assertThat(amWord.getGov(), is("programmer"));
		assertThat(amWord.getDepIndex(), is(2));
		assertThat(amWord.getGovIndex(), is(4));
		assertThat(amWord.getRelation(), is("cop"));
		
		assertThat(aWord.getDep(), is("a"));
		assertThat(aWord.getGov(), is("programmer"));
		assertThat(aWord.getDepIndex(), is(3));
		assertThat(aWord.getGovIndex(), is(4));
		assertThat(aWord.getRelation(), is("det"));
		
		assertThat(programmerWord.getDep(), is("programmer"));
		assertThat(programmerWord.getGov(), is(""));
		assertThat(programmerWord.getDepIndex(), is(4));
		assertThat(programmerWord.getGovIndex(), is(0));
		assertThat(programmerWord.getRelation(), is("root"));
	}
}
