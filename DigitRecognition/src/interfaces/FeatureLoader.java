package interfaces;

import java.util.List;
import dataObjects.DataSample;

// Define interfaces that can be further implemented to load different data set
public interface FeatureLoader {
	// Load feature files by splitting samples with trainingPercentage of them
	// as training samples and the rest as testing samples.
	public void load(float trainingPercentage);
	
	// Must return the list of samples for training.
	public List<DataSample> getTrainingSet();
	
	// Must return the list of samples for testing.
	public List<DataSample> getTestingSet();
	
	public String getName();
}
