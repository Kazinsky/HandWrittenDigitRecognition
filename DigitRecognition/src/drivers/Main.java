package drivers;

import clustering.JaccardSimilarity;
import clustering.PaperSimilarity;
import clustering.RogersTanimoto;
import classifiers.DecisionTree;
import classifiers.NaiveBayes;
import clustering.ClusteringAlgorithm;
import featuresLoaders.NaiveFeatureLoader;
import argumentParser.ArgumentParser;

public class Main {

	public static void main(String[] args) {
		
		try {
			ArgumentParser ap = new ArgumentParser();
			
			ap.addArgument("--naive-loader", new NaiveFeatureLoader("Features\\naive\\feature", 20, 20));
			ap.addArgument("--skeleton-loader", new NaiveFeatureLoader("Features\\skeleton\\feature", 15, 15));
			ap.addArgument("--clustering-paper", new ClusteringAlgorithm(new PaperSimilarity(2.0f, 2.5f)));
			ap.addArgument("--clustering-rt", new ClusteringAlgorithm(new RogersTanimoto()));
			ap.addArgument("--clustering-jaccard", new ClusteringAlgorithm(new JaccardSimilarity()));
			ap.addArgument("--decision-tree", new DecisionTree());
			ap.addArgument("--naive-bayes", new NaiveBayes());

			ap.parse(args);
			
			ap.load();
			ap.execute();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
