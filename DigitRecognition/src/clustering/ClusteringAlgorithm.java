package clustering;

import interfaces.Algorithm;
import java.util.List;
import java.util.ArrayList;
import dataObjects.DataSample;
import enums.DigitClass;

public class ClusteringAlgorithm implements Algorithm{
    private List<Cluster> clusters;
    private float alpha;
    private float beta;
    private float threshold;
    private int kNeighbours;
    
    public ClusteringAlgorithm() {
    	alpha = 2.5f;
		beta = 2.5f;
		threshold = 0.7f;
		clusters = new ArrayList<Cluster>();
		kNeighbours = 1;
    }
    
    public void train(List<DataSample> trainingSet) {
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
				List<Cluster> toRemove = new ArrayList<Cluster>();
				for (Cluster other : clusters) {
					if (toAdd != other && toAdd.getDigitClass() == other.getDigitClass()) {
						t = toAdd.compare(other);
						
						if (t > threshold) {
							// Merge both clusters
							if (newCluster == null)
								newCluster = toAdd.merge(other);
							else
								newCluster = newCluster.merge(other);
								
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
    public DigitClass classify(DataSample ds) {
    	float[] max = new float[kNeighbours];
    	int[] classValue = new int[kNeighbours];

    	for (Cluster cl : clusters) {
    		float t = cl.compare(ds);
    		
    		for (int i = kNeighbours - 1; i >= 0; --i) {
    			if (t > max[i]) {
    				for (int j = i; j > 0; --j) {
    					max[j - 1] = max[j];
    					classValue[j - 1] = classValue[j];
    				}
    				max[i] = t;
    				classValue[i] = cl.getDigitClass().getValue();
    				break;
    			}
    		}
    	}
    	int[] total = {0,0,0,0,0,0,0,0,0,0};
    	int maxVote = 0;
    	int idx = 0;
    	for (int i = 0; i < kNeighbours; ++i) {
    		total[classValue[i]]++;
    		if (total[classValue[i]] > maxVote) {
    			maxVote = total[classValue[i]];
    			idx = classValue[i];
    		}
    	}
    	return DigitClass.values()[idx];
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
    
    public void setKNeighbours(int k) {
    	kNeighbours = k;
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
    
    public String getName() {
    	return "Clustering";
    }
} // End Class
