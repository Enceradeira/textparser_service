package uk.co.jennius.textparser;


import java.util.Iterator;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;

public class LabeledScoredTreeNodeIterator {
	private LabeledScoredTreeNode _tree;

	public LabeledScoredTreeNodeIterator(LabeledScoredTreeNode tree){
		_tree = tree;
	}
	
	private  void visitTree(LabeledScoredTreeNode parent, LabeledScoredTreeNode tree, ILabeledScoredTreeNodeVisitor visitor) throws TextParserException{
		if( tree.isLeaf()){
			Iterator<Label> words = parent.yield().iterator();
			if(words.hasNext()){
				CoreLabel coreLabel = (CoreLabel)words.next();
				visitor.visitLeaf((CoreLabel)coreLabel, tree);	
				if(words.hasNext()){
					throw new TextParserException("parent yielded more than one label");
				}
			}
			else {
				throw new TextParserException("parent didn't yield labels");
			}
		}
		for(Tree child : tree.getChildrenAsList()){
			visitTree(tree,(LabeledScoredTreeNode)child, visitor);
		}
	}
	
	public void visit(ILabeledScoredTreeNodeVisitor visitor) throws TextParserException{
		visitTree(null,_tree,visitor);
	}
}
