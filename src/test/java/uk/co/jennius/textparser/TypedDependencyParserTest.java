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
	@Ignore
	public void getTypedDependencies_ShouldReturnDependenciesInSentence()
	{
		assertThat(false, is(true));
	}
}
