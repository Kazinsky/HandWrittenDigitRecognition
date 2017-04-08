package dataObjects;

import java.util.ArrayList;
import java.util.List;

import enums.DigitClass;

public class DecisionTreeNode{
	
    private List<DecisionTreeNode> children = new ArrayList<>();
    private DecisionTreeNode parent = null;
	private DigitClass digitClass;
    private Feature  feature;

    public DecisionTreeNode(Feature feature) {
        this.feature = feature;
    }
    
    public DecisionTreeNode(DigitClass digitClass) {
        this.feature = null;
        this.digitClass = digitClass;
    }

    public void addChild(DecisionTreeNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Feature feature) {
        DecisionTreeNode newChild = new DecisionTreeNode(feature);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<DecisionTreeNode> children) {
        for(DecisionTreeNode t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<DecisionTreeNode> getChildren() {
        return children;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    private void setParent(DecisionTreeNode parent) {
        this.parent = parent;
    }

    public DecisionTreeNode getParent() {
        return parent;
    }
    
    public DigitClass getDigitClass() {
		return digitClass;
	}

	public void setDigitClass(DigitClass digitClass) {
		this.digitClass = digitClass;
	}
	
	public boolean isLeaf(){
		if(digitClass == null)
			return false;
		else 
			return true;
	}
	

}