package uk.co.jennius.textparser;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;

public interface ILabeledScoredTreeNodeVisitor {
	void visitLeaf(CoreLabel parent, LabeledScoredTreeNode leaf);
}
