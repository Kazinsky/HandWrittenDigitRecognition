package argumentParser;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.Exception;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import interfaces.FeatureLoader;
import interfaces.Algorithm;
import enums.DigitClass;
import dataObjects.DataSample;
import gui.JTablePopupMenuExample;

public class ArgumentParser {
	// Map to store the 
	Map<String, FeatureLoader> featureLoaders;
	Map<String, Algorithm> algorithms;
	
	List<String> loadersToUse;
	List<String> algorithmsToUse;
	
	public ArgumentParser() {
		featureLoaders = new HashMap<String, FeatureLoader>();
		algorithms = new HashMap<String, Algorithm>();
		
		loadersToUse = new ArrayList<String>();
		algorithmsToUse = new ArrayList<String>();
	}
	
	public void parse(String[] args) throws Exception {
		if (args.length < 2)
			throw(new Exception("you must provide at least 2 arguments"));
		for (int i = 0; i < args.length; ++i) {
			if (featureLoaders.get(args[i]) != null) {
				loadersToUse.add(args[i]);
			}
			else if (algorithms.get(args[i]) != null) {
				algorithmsToUse.add(args[i]);
			}
		}
		if (loadersToUse.size() == 0 || algorithmsToUse.size() == 0) {
			throw (new Exception("you must provide at least one feature and one algorithm"));
		}
	}
	
	public FeatureLoader get(String name) {
		return featureLoaders.get(name);
	}
	
	public void addArgument(String argument, FeatureLoader fl) {
		featureLoaders.put(argument, fl);
	}
	
	public void addArgument(String argument, Algorithm algo) {
		algorithms.put(argument, algo);
	}
	
	public void load() {
		for (String flName : loadersToUse) {
			FeatureLoader fl = featureLoaders.get(flName);
			
			System.out.println("Load " + fl.getName());
			fl.load(0.9f);
		}
		
	}
	
	public void execute() {
		List<DataSample> testingSet;
		List<DataSample> trainingSet;
		long startTime;
		long stopTime;
		long elapsedTime;

		for (String flName : loadersToUse) {
			FeatureLoader fl = featureLoaders.get(flName);
			trainingSet = fl.getTrainingSet();
			testingSet = fl.getTestingSet();
			
			System.out.println("Using " + fl.getName());
			for (String alName : algorithmsToUse) {
				Algorithm al = algorithms.get(alName);
				
				System.out.println("Train " + al.getName());
				startTime = System.currentTimeMillis();
				al.train(trainingSet);
				stopTime = System.currentTimeMillis();
				elapsedTime = stopTime - startTime;
				System.out.println("Trained in " + elapsedTime + "ms...");

				System.out.println("Testing with " + al.getName());
				int[] total = new int[10];
				int[] found = new int[10];
				int[][] mistaken = new int[10][10];
				startTime = System.currentTimeMillis();
				int count = 0;
				float nextPrint = 0.1f;
				for (DataSample ds : testingSet) {
					DigitClass d = al.classify(ds);
					//ds.getDigitClass = real digit class
					//d = what the algorithm thought
					total[ds.getDigitClass().getValue()]++;
					if (ds.getDigitClass() == d){
						found[d.getValue()]++;
					}
					else{
						mistaken[ds.getDigitClass().getValue()][d.getValue()]++;
						//the index of this array is [real digit]mistaken for -->[digit] --> {value} times
					}
					count++;
					if ((float)count / testingSet.size() >= nextPrint) {
						System.out.println(nextPrint * 100.0f + "% done...");
						nextPrint += 0.1f;
					}
				}
				stopTime = System.currentTimeMillis();
				elapsedTime = stopTime - startTime;
				System.out.println("Testing finished in " + elapsedTime +"ms...");
				String[] colNames = new String[]{"","0","1","2","3","4","5","6","7","8","9","%"};
				String[][] rowData = new String[10][12];
				for (int i = 0; i < 10; ++i) {
					System.out.println("Found " + found[i] + " out of " + total[i] + " for digit " + DigitClass.values()[i]);
					for (int y = 0; y < 10; ++y){
						if (y!=i)
							System.out.println("digit " +DigitClass.values()[i] +" mistaken for digit "+DigitClass.values()[y]+" "+mistaken[i][y]+" times.");
						rowData[i][y+1] = ""+mistaken[i][y];
						rowData[i][0] = ""+i;
						if (i==y){
							rowData[i][y+1] = ""+found[i]; 
						}
						
					}
					rowData[i][11] = ""+found[i];
				}
				DefaultTableModel model = new DefaultTableModel(rowData, colNames);
				JTable table = new JTable(model);
				JTablePopupMenuExample menu = new JTablePopupMenuExample();
				menu.add(new JScrollPane(table));
				menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				table.setSize(640, 150);
				menu.setLocationRelativeTo(null);
				menu.setVisible(true);
				table.setVisible(true);
				menu.setSize(500,250);
			}
		}
	}
}
