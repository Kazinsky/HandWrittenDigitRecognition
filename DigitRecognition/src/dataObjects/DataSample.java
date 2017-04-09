package dataObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import enums.DigitClass;
import enums.FeatureValues;

public class DataSample {
	
private int id;
private Map<Feature,Integer> data;
private DigitClass digitClass;

public DataSample() {
}

public DataSample(int id, Map<Feature, Integer> data, DigitClass digitClass) {
	this.id = id;
	this.data = data;
	this.digitClass = digitClass;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public Map<Feature, Integer> getData() {
	return data;
}

public void setData(Map<Feature, Integer> data) {
	this.data = data;
}

public DigitClass getDigitClass() {
	return digitClass;
}

public void setDigitClass(DigitClass digitClass) {
	this.digitClass = digitClass;
}

//Returns a DataSample list with it's data split on the provided feature and value
public static List<DataSample> split(List<DataSample> dataSet, Feature featureToSplitOn, FeatureValues featureValue){
	
	List<DataSample> subSet = new ArrayList<DataSample>();
	
	DataSample sample = new DataSample();
	
	for(DataSample item: dataSet){
		
		if(item.data.get(featureToSplitOn) == featureValue.getValue()){
			
			sample.setId(item.getId());
			sample.setData(item.getData());
			sample.setDigitClass(item.getDigitClass());
			
			subSet.add(sample);
		}
	}
	
	return subSet;
}

public Vector<Integer> asVector() {
	Vector<Integer> v = new Vector<Integer>();
	
	for (Map.Entry<Feature, Integer> e : data.entrySet()) {
		v.add(e.getValue());
	}
	return v;
}

@Override
public String toString() {
	return "DataSample [id=" + id + ", data=" + data + ", digitClass=" + digitClass + "]";
}
	

}
