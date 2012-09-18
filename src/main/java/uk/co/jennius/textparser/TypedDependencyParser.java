package uk.co.jennius.textparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import java.util.List;

import uk.co.jennius.textparser.grammar.DisplayDirectoryAndFile;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;


public class TypedDependencyParser {

	private static LexicalizedParserQuery _parserQuery = null;
	private static LexicalizedParserQuery getParserQuery(){
		if( _parserQuery == null){
			initParserQuery(); 
		}
		return _parserQuery;
	}
	
	private static synchronized void initParserQuery(){
		String currentPath = "can't be retrieved";
		try {
			System.err.print("Before getCanonicalPath");
			currentPath = new java.io.File(".").getCanonicalPath();
			System.err.print("Before printStructure");
			DisplayDirectoryAndFile.display(".");
			
		} catch (Exception e) {
			 System.err.print("Exception on getCanonicalPath: "+e.getMessage());
		}

	
		String path = "uk/co/jennius/textparser/grammar/englishPCFG.ser.gz";
		ClassLoader classLoader = TypedDependencyParser.class.getClassLoader();
		InputStream stream = classLoader.getResourceAsStream(path);
		if (stream == null){
			

			
			throw new Error("stream == null :" + currentPath);
		}

		if( _parserQuery == null){

			LexicalizedParser parser = LexicalizedParser.loadModel(path, "-retainTmpSubcategories" /* should lead to better performance when producing typed-dependencies*/);
			_parserQuery =  parser.parserQuery();
		}
	}
	
	private DocumentPreprocessor createDocumentPreprocessor(StringReader reader){
		DocumentPreprocessor preProcessor = new DocumentPreprocessor(reader);
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(false /* tokenize newlines*/, true /* invertible in order to be able to reconstruct original words*/);
		preProcessor.setTokenizerFactory(tokenizerFactory);
		return preProcessor;
	}

	private List<List<HasWord>> getSentences(String text){

		StringReader reader = new StringReader(text);
		try{
			List<List<HasWord>> sentences = new ArrayList<List<HasWord>>();
			DocumentPreprocessor preProcessor = createDocumentPreprocessor(reader);
			for ( List<HasWord> sentence : preProcessor){
				sentences.add(sentence);
			}
			return sentences;
		}
		finally{
			reader.close();
		}	
	}
	
	private List<LabeledScoredTreeNode> getSentencesTree(String text){
		

		List<LabeledScoredTreeNode> trees = new ArrayList<LabeledScoredTreeNode>();
		StringReader reader = new StringReader(text);
		try{
			List<List<HasWord>> sentences = getSentences(text);
			for ( List<HasWord> sentence : sentences){
				LexicalizedParserQuery query = getParserQuery();
				query.parse(sentence);
				trees.add((LabeledScoredTreeNode) query.getBestParse());
			}
			return trees;
		}
		finally{
			reader.close();
		}
	}

	public List<Sentence> getTypedDependencies(String text) throws TextParserException {
		List<Sentence> result = new ArrayList<Sentence>();
		List<LabeledScoredTreeNode> sentences = getSentencesTree(text);
		
		for( LabeledScoredTreeNode sentence : sentences){
			LabeledScoredTreeNodeIterator iterator = new LabeledScoredTreeNodeIterator(sentence);
			StringBuilder sentenceBuilder = new StringBuilder();
			iterator.visit(new WordConcatenater(sentenceBuilder));
			result.add(new Sentence(sentenceBuilder.toString().trim()));
		}
		
		return result;
	}

}
