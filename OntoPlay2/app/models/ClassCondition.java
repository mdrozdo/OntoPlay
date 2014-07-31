package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ClassCondition {
	private String classUri;
	private List<PropertyValueCondition> propertyConditions = new ArrayList<PropertyValueCondition>();

	public ClassCondition(){
		
	}
	
	public ClassCondition(String classUri){
		this.classUri = classUri;
	}

		
	public String getClassUri() {
		return classUri;
	}

	public List<PropertyValueCondition> getPropertyConditions() {
		return propertyConditions;
	}

	public void addProperty(PropertyValueCondition propertyCondition) {		
		propertyConditions.add(propertyCondition);
	}
}
