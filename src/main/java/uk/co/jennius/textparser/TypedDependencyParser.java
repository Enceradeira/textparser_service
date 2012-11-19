package uk.co.jennius.textparser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
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

	private static LexicalizedParser _parser = null;
	private TypedDependencyOptions option;

	public TypedDependencyParser(TypedDependencyOptions options) {
		this.option = options;
	}

	private static LexicalizedParser getParser() {
		if (_parser == null) {
			initParser();
		}
		return _parser;
	}

	private static synchronized void initParser() {
		if (_parser == null) {

			_parser = LexicalizedParser.loadModel(
					"englishPCFG.ser.gz", "-retainTmpSubcategories" /*
																	 * should
																	 * lead to
																	 * better
																	 * performance
																	 * when
																	 * producing
																	 * typed
																	 * -dependencies
																	 */);
			
		}
	}

	private DocumentPreprocessor createDocumentPreprocessor(StringReader reader) {
		DocumentPreprocessor preProcessor = new DocumentPreprocessor(reader);
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
				false /* tokenize newlines */, true /*
												 * invertible in order to be
												 * able to reconstruct original
												 * words
												 */);
		preProcessor.setTokenizerFactory(tokenizerFactory);
		return preProcessor;
	}

	private List<List<HasWord>> getSentences(String text) {

		StringReader reader = new StringReader(text);
		try {
			List<List<HasWord>> sentences = new ArrayList<List<HasWord>>();
			DocumentPreprocessor preProcessor = createDocumentPreprocessor(reader);
			for (List<HasWord> sentence : preProcessor) {
				sentences.add(sentence);
			}
			return sentences;
		} finally {
			reader.close();
		}
	}

	private List<LabeledScoredTreeNode> getSentencesTree(String text) {

		List<LabeledScoredTreeNode> trees = new ArrayList<LabeledScoredTreeNode>();
		StringReader reader = new StringReader(text);
		try {
			List<List<HasWord>> sentences = getSentences(text);
			for (List<HasWord> sentence : sentences) {
				LexicalizedParserQuery query = getParser().parserQuery();
				query.parse(sentence);
				trees.add((LabeledScoredTreeNode) query.getBestParse());
			}
			return trees;
		} finally {
			reader.close();
		}
	}

	private Sentence createSentence(LabeledScoredTreeNode sentenceTree,
			LinkedList<TypedDependencyDesc> dependencies) throws TextParserException {
		LabeledScoredTreeNodeIterator iterator = new LabeledScoredTreeNodeIterator(
				sentenceTree);
		SentenceBuilder sentenceBuilder = new SentenceBuilder(dependencies,
				option);
		iterator.visit(sentenceBuilder);
		return sentenceBuilder.getSentence();
	}

	private TypedDependencyDesc createDependencyDescriptor(TypedDependency dependency) {
		String gov = dependency.gov().label().originalText();
		String dep = dependency.dep().label().originalText();
		int govIndex = dependency.gov().index() -1;  // because we prefer 0-based index 
		int depIndex = dependency.dep().index() -1;
		String relation = dependency.reln().getShortName();
		TypedDependencyDesc word = new TypedDependencyDesc(dep, gov, depIndex, govIndex, relation);
		return word;
	}

	public List<Sentence> getTypedDependencies(String text)
			throws TextParserException {
		List<Sentence> result = new ArrayList<Sentence>();
		List<LabeledScoredTreeNode> sentenceTrees = getSentencesTree(text);

		for (LabeledScoredTreeNode sentenceTree : sentenceTrees) {
			LinkedList<TypedDependencyDesc> dependencies = new LinkedList<TypedDependencyDesc>();
			EnglishGrammaticalStructure structure = new EnglishGrammaticalStructure(
					sentenceTree);
			for (TypedDependency dependency : structure
					.typedDependenciesCCprocessed()) {
				TypedDependencyDesc word = createDependencyDescriptor(dependency);
				dependencies.add(word);
			}

			Sentence sentence = createSentence(sentenceTree, dependencies);

			result.add(sentence);
		}
		return result;
	}
}
