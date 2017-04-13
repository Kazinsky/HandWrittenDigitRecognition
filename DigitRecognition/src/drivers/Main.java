package drivers;

import classifiers.DecisionTree;
import classifiers.MultiLayerPerceptron;
import classifiers.NaiveBayes;
import clustering.ClusteringAlgorithm;
import featuresLoaders.NaiveFeatureLoader;
import argumentParser.ArgumentParser;

public class Main {

	public static void main(String[] args) {
		
		try {
			ArgumentParser ap = new ArgumentParser();			
			ap.addArgument("--naive-loader", new NaiveFeatureLoader());
			ap.addArgument("--clustering", new ClusteringAlgorithm());
			ap.addArgument("--decision-tree", new DecisionTree());
			ap.addArgument("--naive-bayes", new NaiveBayes());						
			ap.addArgument("--mlp", new MultiLayerPerceptron(10,3)); //# hidden neurons, # hidden layers

			ap.parse(args);
			
			ap.load();
			ap.execute();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
