package featuresLoaders;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import dataObjects.DataSample;
import dataObjects.Feature;
import enums.DigitClass;
import enums.FeatureValues;
import interfaces.FeatureLoader;

public class NaiveFeatureLoader implements FeatureLoader {
	private List<DataSample> trainingSet;
	private List<DataSample> testingSet;
	
	public NaiveFeatureLoader() {
		trainingSet = new ArrayList<DataSample>();
		testingSet = new ArrayList<DataSample>();
	}
	
	// Load features extracted with naive algorithm.
	// Try to look for the files in Features/naive/.
	// Files must follow this pattern:
	// number_of_samples
	// number_of_columns number_of_rows
	// each sample on one line, values can be seperated by whitespace
	public void load(float trainingPercentage) {
		// All the strings used in the parsing
		String FeaturesLocation = "Features/naive/feature";
		String FeatureFormat = "PIXEL";
		String sample;
		
		FileReader fr = null;
		BufferedReader br = null;
		
		final int SAMPLES_PER_DIGIT = 1000;
		final int TRAINING_SAMPLES = (int) (1000 * trainingPercentage);
		final int NUMBER_OF_CLASSES = 10;
		final int NUMBER_OF_COLUMNS = 20;
		final int NUMBER_OF_ROWS = 20;

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
				currentClass = DigitClass.values()[i];
				
				fr = new FileReader(FeaturesLocation + i + ".txt");
				br = new BufferedReader(fr);
				
				sample = "";

			    //Read File line by line
				while((line = br.readLine()) != null) {
					
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
								trainingSet.add(currentSample);
							else {
								testingSet.add(currentSample);
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
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			//Close all open buffers and readers/writers
			try{
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
				
			}catch (IOException e){
				e.printStackTrace();
			}			
		}
	}
	
	public List<DataSample> getTrainingSet() {
		return trainingSet;
	}
	
	public List<DataSample> getTestingSet() {
		return testingSet;
	}
	
	public String getName() {
		return "Naive features";
	}
}
