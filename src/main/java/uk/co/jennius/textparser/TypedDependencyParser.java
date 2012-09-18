package uk.co.jennius.textparser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.EnglishGrammaticalStructure;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.TypedDependency;


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

			LexicalizedParser parser = LexicalizedParser.loadModel("englishPCFG.ser.gz", "-retainTmpSubcategories" /* should lead to better performance when producing typed-dependencies*/);
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
	
	private Sentence createSentence(LabeledScoredTreeNode sentenceTree)
			throws TextParserException {
		LabeledScoredTreeNodeIterator iterator = new LabeledScoredTreeNodeIterator(sentenceTree);
		StringBuilder sentenceBuilder = new StringBuilder();
		iterator.visit(new WordConcatenater(sentenceBuilder));
		Sentence sentence = new Sentence(sentenceBuilder.toString().trim());
		return sentence;
	}

	public List<Sentence> getTypedDependencies(String text) throws TextParserException {
		List<Sentence> result = new ArrayList<Sentence>();
		List<LabeledScoredTreeNode> sentenceTrees = getSentencesTree(text);
		
		for( LabeledScoredTreeNode sentenceTree : sentenceTrees){
			Sentence sentence = createSentence(sentenceTree);
			
			EnglishGrammaticalStructure structure = new EnglishGrammaticalStructure(sentenceTree);
			for( TypedDependency dependency: structure.typedDependenciesCCprocessed()){
				 Word word = createWord(dependency);
				 sentence.addWord(word);
			}
			
			result.add(sentence);
		}
		return result;
	}

	private Word createWord(TypedDependency dependency) {
		String gov = dependency.gov().label().originalText();
		 String dep = dependency.dep().label().originalText();
		 int govIndex = dependency.gov().index();
		 int depIndex = dependency.dep().index();
		 String relation = dependency.reln().getShortName();
		 Word word = new Word(dep,gov,depIndex,govIndex,relation);
		return word;
	}


}
