package interfaces;

import java.util.List;
import dataObjects.DataSample;
import enums.DigitClass;

public interface Algorithm {
	public void train(List<DataSample> trainingSet);
	public DigitClass classify(DataSample ds);
	public String getName();
}
