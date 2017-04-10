package clustering;

import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import dataObjects.DataSample;
import enums.DigitClass;

public class Cluster {
    private DataSample center;
    private List<DataSample> samples;
    private float alpha;
    private float beta;
    
    public Cluster(DataSample ds, float a, float b) {
    	center = ds;
    	alpha = a;
    	beta = b;
    	samples = new LinkedList<DataSample>();
    	
    	samples.add(ds);
    }
    
    public Cluster() {
    	
    }
    
    public Cluster merge(Cluster cl) {
    	Cluster newCluster = new Cluster();
    	
    	newCluster.samples = new LinkedList<DataSample>(samples);
    	newCluster.samples.addAll(cl.samples);
    	//newCluster.computeCenter();
    	
    	return newCluster;
    }
    
    // Returns a matrix computing the differences between the two vectors
    // In idx = 0, number of attributes where both = 1
    // In idx = 1, number of attributes where second = 0 and first = 1
    // In idx = 2, number of attributes where second = 1 and first = 0
    // In idx = 3, number of attributes where both = 0
    private int[] buildMatrix(Vector<Integer> first, Vector<Integer> second) {
    	int[] res = new int[4];
    	
    	for (int i = 0; i < first.size(); ++i) {
    		if (first.get(i) == second.get(i) && first.get(i) == 1) {
    			res[0]++;
    		}
    		else if (first.get(i) == second.get(i) && first.get(i) == 0) {
    			res[3]++;
    		}
    		else if (first.get(i) == 1) {
    			res[1]++;
    		}
    		else {
    			res[2]++;
    		}
    	}
    	
    	return res;
    }
    
    private float compare(DataSample first, DataSample second) {
    	int[] mat = buildMatrix(first.asVector(), second.asVector());
    	
    	return ((alpha * mat[0]) + (beta * mat[3])) / ((alpha * mat[0]) + (beta * mat[3]) + 2 * (mat[1] + mat[2]));
    }
    
    // Compare ds to cluster center and return the coefficient of similarity.
    public float compare(DataSample ds) {
    	return compare(center, ds);
    }
    
    // Compare other to this cluster using their centers.
    public float compare(Cluster other) {
    	return compare(center, other.center);
    }
    
    public void computeCenter() {
    	float maxAverage = 0.0f;
    	DataSample d = null;
    	
    	for (DataSample ds : samples) {
    		float average = 0;
    		for (DataSample other : samples) {
    			average += compare(ds, other);
    		}
    		average /= samples.size();
    		if (average > maxAverage) {
    			maxAverage = average;
    			d = ds;
    		}
    	}
    	center = d;
    }
    
    // Add sample to this cluster
    public void add(DataSample ds) {
    	samples.add(ds);
    	if (samples.size() > 2) {
    		//computeCenter();
    	}
    }
    
    // Returns the DigitClass corresponding to this cluster
    public DigitClass getDigitClass () {
    	return center.getDigitClass();
    }
    
}
