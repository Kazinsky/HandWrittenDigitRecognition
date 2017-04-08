package enums;

public enum FeatureValues {
	
	White (0), Black(1);
	
	private int value;
	
	private FeatureValues(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
}
