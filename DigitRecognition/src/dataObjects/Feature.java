package dataObjects;

import java.util.Objects;

public class Feature {
	
	String name;
	
	public Feature(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	//Overrides to be able to Compare two objects of this class
	 @Override
	    public int hashCode() {
	        return Objects.hash(name);
	    }
	 
	   @Override
	    public boolean equals(Object other) {

	        if (other == this) return true;
	        if (!(other instanceof Feature)) {
	            return false;
	        }
	        Feature feature = (Feature) other;
	        
	        return Objects.equals(name, feature.name);
	    }

	@Override
	public String toString() {
		return "[" + name + "]";
	}



}
