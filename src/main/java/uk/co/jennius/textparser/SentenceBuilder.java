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
	private LinkedList<Dependency> dependencies;
	// options influencing how the Sentence is constructed
	private TypedDependencyOptions option;
	// maps the original index to the actually used index per dependency (index
	// shifts as "fillings" are inserted)
	private HashMap<Integer, Integer> originalToModifiedIndexMapping = new HashMap<Integer, Integer>();

	public SentenceBuilder(LinkedList<Dependency> dependencies,
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

		Dependency next = dependencies.peek();
		String dep = next.getText();
		if (text.endsWith(dep)) {
			int startIndex = text.indexOf(dep);
			String filling = text.substring(0, startIndex);
			if (!(filling == null || filling.length() == 0)) {
				addFilling(filling);
			}
			// it's a dependency
			addDependency(next);
			dependencies.poll();
		} else {
			// it's a filling and hopefully next visit is a filling and a
			// dependency
			addFilling(text);
		}
	}

	private void addFilling(String text) {
		if (option == TypedDependencyOptions.PreserveSentenceStructure) {
			sentence.add(new Filling(text, currentIndex));
			currentIndex++;
		}
	}

	private void addDependency(Dependency dep) {

		originalToModifiedIndexMapping.put(dep.getIndex(), currentIndex);
		// adds the dependency at the currentIndex, gotIndex might be wrong at
		// this time because getGovIndex() doesn't account for fillings
		// govIndex will be corrected at the end, when we know how all the
		// dependencies were offset
		sentence.add(new Dependency(dep.getText(), dep.getGov(), currentIndex,
				dep.getGovIndex(), dep.getRelation()));
		currentIndex++;
	}

	public void visitsFinished() {
		// we now have to update the govIndex since fillings mighty have been
		// inserted between the dependencies
		for (Dependency dep : sentence.getDependencies()) {
			if (dep.getRelation() != "root") {
				dep.setGovIndex(originalToModifiedIndexMapping.get(dep
						.getGovIndex()));
			}
		}

	}

	public Sentence getSentence() {
		return sentence;
	}
}
