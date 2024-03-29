package classifiers;

import java.util.List;
import java.util.ArrayList;

import dataObjects.DataSample;
import dataObjects.DecisionTreeNode;
import dataObjects.Feature;
import enums.DigitClass;
import enums.FeatureValues;
import interfaces.Algorithm;

public class DecisionTree implements Algorithm{

private final int NUMBER_OF_CLASSES = 10;
	
//Root of the tree
private DecisionTreeNode root;

//Constructor
public DecisionTree() {
}

//Returns The Root Node of the Decision tree
public DecisionTreeNode buildTree(List<DataSample> dataSet, List<Feature> unusedFeatures){
	
	//Check if current data set is all the same class
	if(areAllSameClass(dataSet)){
		return  new DecisionTreeNode(dataSet.get(0).getDigitClass());
	}
	
	//Feature that will be used to split on and assigned to the next child
	Feature bestSplitFeature = new Feature("");
	
	int index = 0;
	
	//Entropy value
	double smallestEntropy = -1;
	
	//If there is still data but no more unused Features
	if(unusedFeatures.isEmpty()){
		return  new DecisionTreeNode(getMajorityClassValue(dataSet));
	}
	
	//For each unused feature find the one with the smallest entropy value
	for(int i = 0; i< unusedFeatures.size();i++){
		
		double currentEntropy = calculateEntropy(NUMBER_OF_CLASSES, DataSample.split(dataSet, unusedFeatures.get(i), FeatureValues.Black));
		
		if(currentEntropy < smallestEntropy || smallestEntropy == -1){
			smallestEntropy = currentEntropy;
			bestSplitFeature.setName(unusedFeatures.get(i).getName());
			index = i;
		}
			
	}
	
	//Remove best split feature from unusedFeatures
	unusedFeatures.remove(index);
	
	//Add new Node with best feature
	DecisionTreeNode featureNode = new DecisionTreeNode(bestSplitFeature);
	
	//Create child for each Feature values 
	
	for(FeatureValues value: FeatureValues.values()){
		
		//Split data based on feature and current Feature value
		List<DataSample> split = DataSample.split(dataSet, bestSplitFeature, value);
		
		//If this split has no elements then make it a leaf and put class based on the current dataSet
		if(split.isEmpty()){
			featureNode.addChild(new DecisionTreeNode(getMajorityClassValue(dataSet)));
		}
		else{
			//Do the same we just did but now with the child node and data
			featureNode.addChild(buildTree(split,unusedFeatures));
		}
	}
	return featureNode;
	
}

public DecisionTreeNode getRoot(){
	return root;
}

// Provide a List of Training data + List of all possible features
public void train(List<DataSample> trainingData){
	List<Feature> features = new ArrayList<Feature>(trainingData.get(0).getData().keySet());
	
	root = buildTree(trainingData,features);
}

//Used to determine the class from a decision tree by sending a Datasample
public DigitClass classify(DataSample data){
	DecisionTreeNode node = root;
	
	//While we don't arrive at a leaf
	while(!node.isLeaf()){
	
		//if data's feature value is white then go to left child else go to right child
		if(data.getData().get(node.getFeature()) == FeatureValues.White.getValue()){
			node = node.getChildren().get(0);
		}
		else{
			node = node.getChildren().get(1);
		}
			
	}
	
	//Return leaf class type
	return node.getDigitClass();
}

//Calculates the entropy value which is used to select the best attribute to split next
private double calculateEntropy(int NumberOfClasses, List<DataSample> dataSet){
	
	double entropy = 0;
	int count = 0;
	double prob = 0;
	
	if(dataSet.size() > 0){
		
		//For each class
		for (int i = 0; i < NumberOfClasses; i++){
			
			//Count the number of data elements in each class
			count = 0;
			
			//For each Feature in the set
			for(DataSample item: dataSet){
				
				//Count the number of elements in this class
				if(item.getDigitClass() == DigitClass.values()[i]){
					count++;
				}
				
			}
			
			//Calculate the probablity of this class 
			prob = count / (double)dataSet.size();
			
			if(count > 0){
				entropy += -prob * (Math.log(prob) / Math.log(2));
			}
			//-prob * (Math.log(prob) / Math.log(2));
			//prob * (Math.log(1/prob)/Math.log((2)))
		}
	}
	
	return entropy;
	
}

private DigitClass getMajorityClassValue(List<DataSample> dataSet){

	int biggestCount = 0;
	int majorityClass = 0;
	
	int[] classCounts = new int[DigitClass.values().length];
	
	for(int i =0; i< DigitClass.values().length; i++){
		
		for(DataSample sample: dataSet){
			if(sample.getDigitClass() == DigitClass.values()[i])
				classCounts[i]++;
		}
	}
	
	for(int i = 0; i< classCounts.length; i++){
		
		if(biggestCount < classCounts[i]){
			biggestCount = classCounts[i];
			majorityClass = i;
		}
	}
	
	return DigitClass.values()[majorityClass];

}

//Get Class of first elements and see if all other elements have same class if they do return true else return false
private boolean areAllSameClass(List<DataSample> dataSet){

	DigitClass classToCheck = null;
	
	if(dataSet.size() > 0)
		classToCheck = dataSet.get(0).getDigitClass();
	
	
		for(DataSample sample: dataSet){
			if(sample.getDigitClass().getValue() != classToCheck.getValue())
				return false;
				
		}
		
	return true;

}

public void printTree() {
    printSubtree(root);
}

public void printSubtree(DecisionTreeNode node) {
    if (!node.getChildren().isEmpty() && node.getChildren().get(0) != null) {
        printTree(node.getChildren().get(0), true, "");
    }
    printNodeValue(node);
    if (node.getChildren().size() > 1 && node.getChildren().get(1) != null) {
        printTree(node.getChildren().get(1), false, "");
    }
}

private void printNodeValue(DecisionTreeNode node) {
    if (node.isLeaf()) {
        System.out.print(node.getDigitClass());
    } else {
        System.out.print(node.getFeature().getName());
    }
    System.out.println();
}

private void printTree(DecisionTreeNode node, boolean isRight, String indent) {
    if (!node.getChildren().isEmpty() && node.getChildren().get(0) != null) {
        printTree(node.getChildren().get(0), true, indent + (isRight ? "        " : " |      "));
    }
    System.out.print(indent);
    if (isRight) {
        System.out.print(" /");
    } else {
        System.out.print(" \\");
    }
    System.out.print("----- ");
    printNodeValue(node);
    if (node.getChildren().size() > 1 && node.getChildren().get(1) != null) {
        printTree(node.getChildren().get(1), false, indent + (isRight ? " |      " : "        "));
    }
}

public String getName() {
	return "Decision tree";
}

}// End Class
