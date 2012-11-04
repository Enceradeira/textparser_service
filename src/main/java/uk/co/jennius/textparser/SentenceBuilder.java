package uk.co.jennius.textparser;

import java.util.LinkedList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;

public class SentenceBuilder implements ILabeledScoredTreeNodeVisitor {
	// the current index incremented every time a dependency or filling is added
	// to the sentence
	private int currentIndex = 0;
	// the sentence being constructed
	private Sentence sentence;
	// the dependencies from which the sentence is constructed
	private LinkedList<TypedDependencyDesc> dependencies;
	// options influencing how the Sentence is constructed
	private TypedDependencyOptions option;
	// maps the original index to the actually used index per dependency (index
	// shifts as "fillings" are inserted)
	private HashMap<Integer, Integer> originalToModifiedIndexMapping = new HashMap<Integer, Integer>();

	public SentenceBuilder(LinkedList<TypedDependencyDesc> dependencies,
			TypedDependencyOptions option) {
		sentence = new Sentence();
		this.dependencies = dependencies;
		this.option = option;
	}

	public void visitLeaf(CoreLabel parent, LabeledScoredTreeNode leaf) {
		String text = parent.before() + parent.originalText();

		if (sentence.isEmpty()) {
			// trim leading whitespace
			text = StringUtils.stripStart(text, " ");
			if (StringUtils.isEmpty(text)) {
				return;
			}
		}

		if (dependencies.size() == 0) {
			addFilling(text);
			return;
		}

		TypedDependencyDesc next = dependencies.peek();

		if (text.endsWith(next.getText())) {
			int startIndex = text.indexOf(next.getText());
			String filling = text.substring(0, startIndex);
			if (!(filling == null || filling.length() == 0)) {
				addFilling(filling);
			}

			addWord(next);
		} else {
			// it's a filling and hopefully next visit is a filling and a
			// dependency
			addFilling(text);
		}
	}

	private void addWord(TypedDependencyDesc next) {
		// the word index as parsed by the Stanford-Parser
		int origWordIndex = next.getIndex();

		Word word = new Word(next.getText(), currentIndex);
		sentence.add(word);

		originalToModifiedIndexMapping.put(origWordIndex, currentIndex);
		// adds the dependency at the currentIndex, getIndex might be wrong at
		// this time because getIndex() doesn't account for fillings. govIndex
		// is wrong as well but will be corrected at the end when we know how
		// all the
		// dependencies were offset
		currentIndex++;

		// Consume all Dependencies that are related to the added word (in most
		// cases there is only one)
		do {
			Dependency dependency = new Dependency(next.getGov(),
					next.getGovIndex(), next.getRelation());
			word.add(dependency);
			dependencies.poll();
			next = dependencies.peek();
		} while (next != null && next.getIndex() == origWordIndex);

	}

	private void addFilling(String text) {
		if (option == TypedDependencyOptions.PreserveSentenceStructure) {
			sentence.add(new Filling(text, currentIndex));
			currentIndex++;
		}
	}

	public void visitsFinished() {
		// we now have to update the govIndex since fillings mighty have been
		// inserted between the dependencies
		for (Word word : sentence.getWords()) {
			for (Dependency dep : word.getDependencies()) {
				if (dep.getRelation() != "root") {
					dep.setGovIndex(originalToModifiedIndexMapping.get(dep
							.getGovIndex()));
				}
			}
		}
	}

	public Sentence getSentence() {
		return sentence;
	}
}
