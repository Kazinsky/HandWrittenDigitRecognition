package clustering;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import dataObjects.DataSample;
import enums.DigitClass;
import enums.FeatureValues;

public class ClusteringAlgorithm {
    private List<Cluster> clusters;
    private float alpha;
    private float beta;
    private float threshold;
    
    public ClusteringAlgorithm(List<DataSample> trainingSet) {
    	alpha = 2.5f;
		beta = 2.5f;
		threshold = 0.7f;
		clusters = new LinkedList<Cluster>();
		
		train(trainingSet);
    }
    
    private void train(List<DataSample> trainingSet) {
    	boolean isAdd;
    	int i = 0;
		for (DataSample ds : trainingSet) {
			isAdd = false;
			for (Cluster c : clusters) {
				float t = c.compare(ds);
				if (t >= threshold) {
					c.add(ds);
					isAdd = true;
					break;
				}
			}
			if (isAdd == false) {
				clusters.add(new Cluster(ds, alpha, beta));
			}
			else {
				/*boolean remove;
				for (Iterator<Cluster> it = clusters.listIterator(); it.hasNext(); ) {
					Cluster c = it.next();
					remove = false;
					for (Iterator<Cluster> it2 = clusters.listIterator(); it.hasNext(); ) {
						Cluster other = it2.next();
						if (c != other) {
							float t = c.compare(other);
							
							if (t < threshold) {
								// Merge both clusters
								remove = true;
							}
						}
					}
				}*/
			}
		}
		System.out.println("Finished");
    }

    // Try to guess to which digits belong ds
    public DigitClass test(DataSample ds) {
    	return DigitClass.Zero;
    }

    // Change alpha value
    public void setAlpha(float a) {
    	alpha = a;
    }

    // Change beta value
    public void setBeta(float b) {
    	beta = b;
    }

    // Change threshold value
    public void setThreshold(float t) {
    	threshold = t;
    }
    
    public int getNumberClusterOf(DigitClass dc) {
    	int count = 0;
    	
    	for (Cluster c : clusters) {
    		if (c.getDigitClass() == dc) {
    			count++;
    		}
    	}
    	return count;
    }
} // End Class
