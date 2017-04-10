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
    	float maxThreshold;
    	float t;
    	int done = 0;
    	float nextPrint = 0.1f;
    	
    	for (DataSample ds : trainingSet) {
	    	Cluster toAdd = null;
			maxThreshold = 0;
			for (Cluster c : clusters) {
				t = c.compare(ds);
				if (t >= threshold && t > maxThreshold) {
					maxThreshold = t;
					toAdd = c;
				}
			}
			if (toAdd == null) {
				clusters.add(new Cluster(ds, alpha, beta));
			}
			else {
				Cluster newCluster = null;
				toAdd.add(ds);
				toAdd.computeCenter();
				List<Cluster> toRemove = new LinkedList<Cluster>();
				for (Cluster other : clusters) {
					if (toAdd != other) {
						t = toAdd.compare(other);
						
						if (t > threshold) {
							// Merge both clusters
							/*if (newCluster == null)
								newCluster = toAdd.merge(other);
							else
								newCluster = newCluster.merge(other);
								*/
							toRemove.add(other);
						}
					}
				}
				if (newCluster != null) {
					clusters.removeAll(toRemove);
					clusters.remove(toAdd);
					toAdd = null;
					clusters.add(newCluster);
				}
			}
			done++;
			if ((float)done / trainingSet.size() >= nextPrint) {
				System.out.println(nextPrint * 100.0f + "% done...");
				nextPrint += 0.1f;
			}
		}
    }

    // Try to guess to which digits belong ds
    public DigitClass test(DataSample ds) {
    	//float[] max = { 0.0f, 0.0f, 0.0f };
    	//int[] classValue = { -1, -1, -1 };
    	float max = 0.0f;
    	DigitClass dc = null;
    	
    	for (Cluster cl : clusters) {
    		float t = cl.compare(ds);
    		
    		if (t > max) {
    			max = t;
    			dc = cl.getDigitClass();
    		}
    	}
    	return dc;
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
