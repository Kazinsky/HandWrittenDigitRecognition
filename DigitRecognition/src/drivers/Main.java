package drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
		
		//Consts
		final int NUMBER_OF_CLASSES = 10;
		final int NUMBER_OF_COLUMNS = 20;
		final int NUMBER_OF_ROWS = 20;
		
		//Readers to read files
		FileReader fileReader = null;
		BufferedReader bufferReader = null;
		
		//Data objects to store data
		List<DataSample> featuresDataSet = new ArrayList<DataSample>();
		
		/***Load Data From Files**/
		try{
			//Variables to store data
			DigitClass currentClass;
			char currentChar = ' ';
			int charValue = 0;
			
			//Counts
			int sampleCount = 0;
			int currentRow = 0;
			int currentCol= 0;
			
			//For each class we have a feature file
			for(int i = 0; i< NUMBER_OF_CLASSES; i++ ){
			
				currentClass = DigitClass.values()[i];
				
			    //Read File
				fileReader = new FileReader(FeaturesLocation + i + ".txt");
				bufferReader = new BufferedReader(fileReader);
				
				//Read input from the file and store into char

				while((charValue = bufferReader.read()) != -1){
					
					currentChar = (char)charValue;
					
					//Data sample
					DataSample currentSample = new DataSample();
					//Data set used to store the individual features and their values
					Map<Feature,Integer> dataSet = new Hashtable<Feature,Integer>();
					
					currentSample.setId(sampleCount);
					currentSample.setDigitClass(currentClass);
					
					currentRow = 0;
					
					//For Each row in the Matrix of features
					while(currentRow < NUMBER_OF_ROWS){
						
						currentCol = 0;
						
						//For each collumn of the matrix
						while(currentCol < NUMBER_OF_COLUMNS){
							
								if(currentChar  == Integer.toString(FeatureValues.Black.getValue()).toCharArray()[0]){
									dataSet.put(new Feature(FeatureFormat + currentRow + ":" + currentCol), FeatureValues.Black.getValue());
									currentCol++;
								}
								else if(currentChar == Integer.toString(FeatureValues.White.getValue()).toCharArray()[0]){
									dataSet.put(new Feature(FeatureFormat + currentRow + ":" + currentCol), FeatureValues.White.getValue());
									currentCol++;
								}
								
								//Read next Character
								if((charValue = bufferReader.read()) != -1){
									currentChar = (char)charValue;
								}
								else{
									currentCol++;
								}
								
							}

						currentRow++;
					}
					
					currentSample.setData(dataSet);
					featuresDataSet.add(currentSample);
					sampleCount++;
	
				}
				
			}
			
			//StringBuilder stringToOutput  = new StringBuilder();
			
			/*for(DataSample sample: featuresDataSet){
				
				System.out.println( "ID: " + sample.getId() + "  Class: " + sample.getDigitClass() + sample.getData().toString() + "\n");
			}
			
			System.out.println(stringToOutput);*/
			System.out.println("Finished");
			ClusteringAlgorithm ca = new ClusteringAlgorithm(featuresDataSet);

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
