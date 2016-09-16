package ontoplay.models.angular;

public class Operator {
	private String displayValue;
	private String realValue;
	
	
	
	public Operator(String displayValue, String realValue) {
		this.displayValue = displayValue;
		this.realValue = realValue;
	}
	
	public String getDisplayValue() {
		return displayValue;
	}
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	public String getRealValue() {
		return realValue;
	}
	public void setRealValue(String realValue) {
		this.realValue = realValue;
	}
	
	
}
