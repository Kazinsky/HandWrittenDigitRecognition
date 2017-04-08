package drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dataObjects.DataSample;

public class Main {

	public static void main(String[] args) {
		
		//File Locations
		String FeaturesLocation = "\\Features\\feature";
		
		//Consts
		final int NUMBER_OF_CLASSES = 10;
		
		//Readers to read files
		FileReader fileReader = null;
		BufferedReader bufferReader = null;
		
		//Data objects to store data
		List<DataSample> featuresDataSet = new ArrayList<DataSample>();
		
		/***Load Data From Files**/
		try{
			String currentLine = "";
			
			//For each class we have a feature file
			for(int i = 0; i< NUMBER_OF_CLASSES; i++ ){
				
			    //Read File
				fileReader = new FileReader(FeaturesLocation + i + ".txt");
				bufferReader = new BufferedReader(fileReader);
				
				while ((currentLine = bufferReader.readLine()) != null){
					
					//To DO
				}
			}
		

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
