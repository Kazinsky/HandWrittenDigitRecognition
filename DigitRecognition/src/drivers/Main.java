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

		// Used to store the data for one sample;
		String sample;
		
		//Consts
		final int SAMPLES_PER_DIGIT = 1000;
		final int TRAINING_SAMPLES = (int) (1000 * 0.90f);
		final int NUMBER_OF_CLASSES = 10;
		final int NUMBER_OF_COLUMNS = 20;
		final int NUMBER_OF_ROWS = 20;
		
		//Readers to read files
		FileReader fileReader = null;
		BufferedReader bufferReader = null;
		
		//Data objects to store data
		List<DataSample> trainingDataSet = new ArrayList<DataSample>();
		List<DataSample> testingDataSet = new ArrayList<DataSample>();
		
		/***Load Data From Files**/
		System.out.println("Start loading datas...");
		long startTime = System.currentTimeMillis();
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
			
				int classCount = 0;
				int testingCount = 0;
				currentClass = DigitClass.values()[i];
				
				fileReader = new FileReader(FeaturesLocation + i + ".txt");
				bufferReader = new BufferedReader(fileReader);
				
				sample = "";

			    //Read File line by line
				while((line = bufferReader.readLine()) != null) {
					
					// When we encounter an empty line create the sample
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
							if (classCount < TRAINING_SAMPLES)
								trainingDataSet.add(currentSample);
							else {
								testingDataSet.add(currentSample);
								testingCount++;
							}
							classCount++;
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
			long stopTime = System.currentTimeMillis();
		    long elapsedTime = stopTime - startTime;
			System.out.println("Samples loaded in " + elapsedTime + "ms...");
			
			System.out.println("Init clustering..");
			startTime = System.currentTimeMillis();
			ClusteringAlgorithm ca = new ClusteringAlgorithm(trainingDataSet);
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			System.out.println("Clustering initiated in " + elapsedTime + "ms...");
			
			for (int i = 0; i < 10; i++) {
				DigitClass dc = DigitClass.values()[i];
				
				System.out.println("Digit " + dc + " has " + ca.getNumberClusterOf(dc) + " clusters");
			}
			
			int[] total = new int[NUMBER_OF_CLASSES];
			int[] found = new int[NUMBER_OF_CLASSES];
			for (int i = 0; i < NUMBER_OF_CLASSES; ++i) {
				total[i] = 0;
				found[i] = 0;
			}
			System.out.println("Start classification...");
			int count = 0;
			float nextPrint = 0.1f;
			for (DataSample ds : testingDataSet) {
				DigitClass d = ca.test(ds);
				
				total[ds.getDigitClass().getValue()]++;
				if (ds.getDigitClass() == d)
					found[d.getValue()]++;
				count++;
				if ((float)count / testingDataSet.size() >= nextPrint) {
					System.out.println(nextPrint * 100.0f + "% done...");
					nextPrint += 0.1f;
				}
			}
			
			for (int i = 0; i < NUMBER_OF_CLASSES; ++i) {
				System.out.println("Found " + found[i] + " out of " + total[i] + " for digit " + DigitClass.values()[i]);
			}
			System.out.println("Clustering finished...");

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
