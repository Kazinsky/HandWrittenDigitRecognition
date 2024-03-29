package clustering;

import interfaces.ClusteringSimilarity;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import dataObjects.DataSample;
import enums.DigitClass;

public class Cluster {
    private DataSample center;
    private List<DataSample> samples;
    private List<Float> averages;
    private float alpha;
    private float beta;
    private ClusteringSimilarity function;
    
    public Cluster(ClusteringSimilarity cs, DataSample ds) {
    	center = ds;
    	samples = new ArrayList<DataSample>();
    	averages = new ArrayList<Float>();
    	
    	averages.add(1.0f);
    	samples.add(ds);
    	function = cs;
    }
    
    public Cluster() {
    	center = null;
    }
    
    public Cluster merge(Cluster cl) {
    	DataSample ds;
    	DataSample other;
    	float v;
    	float vOther;
    	float t;
    	Cluster newCluster = new Cluster();
    	
    	newCluster.function = function;
    	
    	newCluster.samples = new ArrayList<DataSample>(samples);
    	newCluster.averages = new ArrayList<Float>(averages);
    	newCluster.alpha = alpha;
    	newCluster.beta = beta;
    	for (int i = 0; i < cl.samples.size(); ++i) {
    		ds = cl.samples.get(i);
    		v = cl.averages.get(i);
    		
    		for (int j = 0; j < newCluster.samples.size(); ++j) {
    			other = newCluster.samples.get(j);
    			vOther = newCluster.averages.get(i);

    			t = compare(ds, other);
    			v += t;
    			vOther += t;
    			
    			newCluster.averages.set(i, vOther);
    		}
    		newCluster.averages.add(v);
    	}
    	
    	newCluster.samples.addAll(cl.samples);
    	newCluster.computeCenter();
    	
    	return newCluster;
    }
    
    // Returns a matrix computing the differences between the two vectors
    // In idx = 0, number of attributes where both = 1
    // In idx = 1, number of attributes where second = 0 and first = 1
    // In idx = 2, number of attributes where second = 1 and first = 0
    // In idx = 3, number of attributes where both = 0
    private int[] buildMatrix(Vector<Integer> first, Vector<Integer> second) {
    	int[] res = new int[4];
    	int fValue;
    	int sValue;
    	
    	for (int i = 0; i < first.size(); ++i) {
    		fValue = first.get(i);
    		sValue = second.get(i);
    		
    		if (fValue == sValue && fValue == 1) {
    			res[0]++;
    		}
    		else if (fValue == sValue && fValue == 0) {
    			res[3]++;
    		}
    		else if (fValue == 1) {
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
    	
    	return function.compute(mat);
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
    	float v;
    	
    	for (int i = 0; i < samples.size(); ++i) {
    		v = averages.get(i) / samples.size() ;
    		
    		if (v > maxAverage) {
    			maxAverage = v;
    			d = samples.get(i);
    		}
    	}
    	center = d;
    }
    
    // Add sample to this cluster
    public void add(DataSample ds) {
    	float dsAverage = 1.0f;
    	DataSample s;
    	for (int i = 0; i < samples.size(); ++i) {
    		s = samples.get(i);
    		float v = averages.get(i);
    		float t = compare(s, ds);
    		
    		v += t;
    		dsAverage += t;
    		
    		averages.set(i, v);
    	}
    	
    	averages.add(dsAverage);
    	samples.add(ds);
    }
    
    // Returns the DigitClass corresponding to this cluster
    public DigitClass getDigitClass () {
    	return center.getDigitClass();
    }
    
}
