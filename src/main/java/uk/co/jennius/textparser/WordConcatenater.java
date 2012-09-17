package uk.co.jennius.textparser;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;

public class WordConcatenater implements ILabeledScoredTreeNodeVisitor {
	private StringBuilder _string;
	public WordConcatenater(StringBuilder string){
		_string = string;
	}
	public void visitLeaf(CoreLabel parent,LabeledScoredTreeNode leaf) {
		_string.append(parent.before() + parent.originalText());
	}	
}
