package uk.co.jennius.textparser;

import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;

public class WordConcatenater implements ILabeledScoredTreeNodeVisitor {
	private StringBuilder _string;
	public WordConcatenater(StringBuilder string){
		_string = string;
	}
	public void visitLeaf(CoreLabel parent,LabeledScoredTreeNode leaf) {
		_string.append(parent.before() + parent.originalText());
	}	
}
