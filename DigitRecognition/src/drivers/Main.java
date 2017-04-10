package drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import classifiers.DecisionTree;
import classifiers.NaiveBayes;
import dataObjects.DataSample;
import dataObjects.Feature;
import enums.DigitClass;
import enums.FeatureValues;
import clustering.ClusteringAlgorithm;

public class Main {

	public static void main(String[] args) {
		
		//File Locations
		String FeaturesLocation = "Features/feature";
		String FeatureFormat = "PIXEL";

		// Used to store the data for one sample;
		String sample;
		
		//Consts
		final int NUMBER_OF_CLASSES = 10;
		final int NUMBER_OF_COLUMNS = 20;
		final int NUMBER_OF_ROWS = 20;
		
		//Readers to read files
		FileReader fileReader = null;
		BufferedReader bufferReader = null;
		
		//Data objects to store data
		List<DataSample> featuresDataSet = new ArrayList<DataSample>();
		List<Feature> allPosibleFeatures = new ArrayList<Feature>();
		
		/***Load Data From Files**/
		try{
			//Variables to store data
			DigitClass currentClass;
			char currentChar = ' ';
			String line;
			
			//Counts
			int sampleCount = 0;
			int currentRow = 0;
			int currentCol= 0;
			
			//For each class we have a feature file
			for(int i = 0; i< NUMBER_OF_CLASSES; i++ ){
			
				currentClass = DigitClass.values()[i];
				
				fileReader = new FileReader(FeaturesLocation + i + ".txt");
				bufferReader = new BufferedReader(fileReader);
				
				sample = "";

			    //Read File line by line
				while((line = bufferReader.readLine()) != null) {
					
					// When we encounter an empty create the sample
					if (line.length() == 0) {
						if (sample.length() == NUMBER_OF_ROWS * NUMBER_OF_COLUMNS) {
							//Data sample
							DataSample currentSample = new DataSample();
							//Data set used to store the individual features and their values
							Map<Feature,Integer> dataSet = new Hashtable<Feature,Integer>();

							currentSample.setId(sampleCount);
							currentSample.setDigitClass(currentClass);

							for (currentRow = 0; currentRow < NUMBER_OF_ROWS; ++currentRow) {
								for (currentCol = 0; currentCol < NUMBER_OF_COLUMNS; ++currentCol) {
									currentChar = sample.charAt(currentRow * NUMBER_OF_COLUMNS + currentCol);
									if (currentChar == '1') {
										dataSet.put(new Feature(FeatureFormat + currentRow + ":" + currentCol), FeatureValues.Black.getValue());
									}
									else if (currentChar == '0') {
										dataSet.put(new Feature(FeatureFormat + currentRow + ":" + currentCol), FeatureValues.White.getValue());
									}
								}
							}
							
							currentSample.setData(dataSet);
							featuresDataSet.add(currentSample);
							sample = "";
						}
					}
					else {
						// Remove all whitespace character and append to the sample
						line = line.replaceAll("\\s+", "");
						sample += line;
					}
				}
				
			}
			
			//Store all possible Feature strings
			for(int i =0; i< NUMBER_OF_ROWS; i++){
				
				for(int j = 0; j < NUMBER_OF_COLUMNS; j++){
					
					allPosibleFeatures.add(new Feature(FeatureFormat + i + ":" + j));
				}
				
			}
			
			
			/*for(DataSample sample: featuresDataSet){
				
				System.out.println( "ID: " + sample.getId() + "  Class: " + sample.getDigitClass() + sample.getData().toString() + "\n");
			}
			*/

			System.out.println("Finished");
			
			DataSample d = featuresDataSet.get(0);

			for (currentRow = 0; currentRow < NUMBER_OF_ROWS; ++currentRow) {
				for (currentCol = 0; currentCol < NUMBER_OF_COLUMNS; ++currentCol) {
					System.out.print(d.getData().get(new Feature(FeatureFormat + currentRow + ":" + currentCol)));
				}
				System.out.println("");
			}
			
			//ClusteringAlgorithm ca = new ClusteringAlgorithm(featuresDataSet);
			
		System.out.println("Finished");
			
		
			
			//NaiveBayes naiveBayes = new NaiveBayes(allPosibleFeatures);
			
			//naiveBayes.train(featuresDataSet);
			
			//System.out.println(naiveBayes.classify(featuresDataSet.get(0)));


			//DecisionTree decisionTree = new DecisionTree();

			//decisionTree.train(featuresDataSet, allPosibleFeatures);
			
			//decisionTree.printTree();
			
			
			
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			//Close all open buffers and readers/writers
			try{
				if (bufferReader != null)
					bufferReader.close();

				if (fileReader != null)
					fileReader.close();
				
			}catch (IOException e){
				e.printStackTrace();
			}
			
		}
	

	}

}
