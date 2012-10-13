package models.propertyConditions;

import java.math.BigDecimal;

import models.PropertyValueCondition;

public class DatatypePropertyCondition extends PropertyValueCondition{
	private String datatypeValue;
	private String operator;
	
	public DatatypePropertyCondition(){
		
	}

	public DatatypePropertyCondition(String propertyUri, String operator, String datatypeValue) {
		super(propertyUri);
		this.operator = operator;
		this.datatypeValue = datatypeValue;
	}

	public void setDatatypeValue(String datatypeValue) {
		this.datatypeValue = datatypeValue;
	}

	public String getDatatypeValue() {
		return datatypeValue;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getOperator() {
		return operator;
	} 
}
