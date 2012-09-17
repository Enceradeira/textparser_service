package uk.co.jennius.textparser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;

public class TypedDependencyParser {

	private static LexicalizedParserQuery _parserQuery = null;
	private static LexicalizedParserQuery getParserQuery(){
		if( _parserQuery == null){
			initParserQuery(); 
		}
		return _parserQuery;
	}
	
	private static synchronized void initParserQuery(){
		if( _parserQuery == null){
			String path = "uk/co/jennius/textparser/grammar/englishPCFG.ser.gz";
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
		List<Tree> dummy = new ArrayList<Tree>();
		for( LabeledScoredTreeNode sentence : sentences){
			LabeledScoredTreeNodeIterator iterator = new LabeledScoredTreeNodeIterator(sentence);
			StringBuilder sentenceBuilder = new StringBuilder();
			iterator.visit(new WordConcatenater(sentenceBuilder));
			result.add(new Sentence(sentenceBuilder.toString().trim()));
		}
		
		return result;
	}

}
