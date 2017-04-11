package classifiers;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import dataObjects.DataSample;
import dataObjects.Feature;
import enums.DigitClass;
import enums.FeatureValues;
import interfaces.Algorithm;

public class NaiveBayes implements Algorithm {
	
	//Data objects to hold counts for probablities
	private int[] totalsInEachClass;
	private int totalNumSamples = 0;
	private int numberOfclasses = DigitClass.values().length;
	private Map<Feature,int[]> totalsFeaturesAndClassWhite = new HashMap<Feature,int[]>();
	private Map<Feature,int[]> totalsFeaturesAndClassBlack = new HashMap<Feature,int[]>();
	private List<Feature> features;

		//Constructor	
	public NaiveBayes(){
		
		totalsInEachClass = new int[numberOfclasses];
	}
	
	private void initFeaturesAndClass(List<Feature> allPossibleFeatures) {
		features = new ArrayList<Feature>(allPossibleFeatures);
		
		for(Feature feature: allPossibleFeatures){
			totalsFeaturesAndClassBlack.put(feature, new int[numberOfclasses]);
			totalsFeaturesAndClassWhite.put(feature, new int[numberOfclasses]);
		}
	}
	
	public void train(List<DataSample> samples){
		
		initFeaturesAndClass(new ArrayList<Feature>(samples.get(0).getData().keySet()));
		
		totalNumSamples = samples.size();
		
		//For each dataSample
		for(DataSample sample: samples){
			
			DigitClass currentSamplesClass = sample.getDigitClass();
			
			totalsInEachClass[currentSamplesClass.getValue()] += 1;
			
			//For each feature
			for(Feature feature: features){
				
				//If this sample's feature is black then we want to count it
				if(sample.getData().get(feature) == FeatureValues.Black.getValue()){
					
					totalsFeaturesAndClassBlack.get(feature)[currentSamplesClass.getValue()] += 1;
				}
				else if(sample.getData().get(feature) == FeatureValues.White.getValue()){
					
					totalsFeaturesAndClassWhite.get(feature)[currentSamplesClass.getValue()] += 1;
				}
			}
		}
	}
	
	public DigitClass classify(DataSample sample){
		
		DigitClass digitClass = null;
		
		double biggestScore = -1.0;
		
		//For each class calculate a score based on this sample and the training it's done
		for(int i = 0; i < numberOfclasses;i++){
			
			//Add the probability for this class
			double score = (float)totalsInEachClass[i]/totalNumSamples;
			
			for(Feature feature: features){
				
				//If this feature in this sample is black then we add it's probability
				if(sample.getData().get(feature) == FeatureValues.Black.getValue()){
					//Add probability of this feature from the training data recorded values
					score *= ((float)totalsFeaturesAndClassBlack.get(feature)[i] + 1)/ (totalsInEachClass[i] + 2);
				}
				else if(sample.getData().get(feature) == FeatureValues.White.getValue()){
					//Add probability of this feature from the training data recorded values
					score *= ((float)totalsFeaturesAndClassWhite.get(feature)[i] + 1)/ (totalsInEachClass[i] + 2);
				}
			}
			
			//if current classes score is the biggest then replace the old biggest
			if(score > biggestScore){
				biggestScore = score;
				digitClass = DigitClass.values()[i];
			}
		}
		
		return digitClass;
	}

	public String getName() {
		return "Naive Bayes classifier";
	}
}
