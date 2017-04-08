package classifiers;

import java.util.List;

import dataObjects.DataSample;
import dataObjects.DecisionTreeNode;
import dataObjects.Feature;
import enums.DigitClass;
import enums.FeatureValues;

public class DecisionTree {

private final int NUMBER_OF_CLASSES = 10;
	
//Root of the tree
private DecisionTreeNode root;


public DecisionTree() {
}

//Returns The Root Node of the Decision tree
public DecisionTreeNode buildTree(List<DataSample> dataSet, List<Feature> unusedFeatures){
	//Feature that will be used to split on and assigned to the next child
	Feature bestSplitFeature = new Feature("");
	
	int index = 0;
	
	//Entropy value
	double smallestEntropy = -1;
	
	//For each unused feature find the one with the smallest entropy value
	for(int i = 0; i< unusedFeatures.size();i++){
		
		double currentEntropy = calculateEntropy(NUMBER_OF_CLASSES,dataSet);
		
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
public void train(List<DataSample> trainingData, List<Feature> features){
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
				entropy += prob * (Math.log(1/prob)/Math.log((2)));
			}
			
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

}// End Class
