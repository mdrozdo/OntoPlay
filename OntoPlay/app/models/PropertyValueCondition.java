package models;

import models.ontologyModel.OntoProperty;


public class PropertyValueCondition {
	private String propertyUri;
	private OntoProperty property;

	public PropertyValueCondition(){
		
	}
	
	public PropertyValueCondition(String propertyUri) {
		this.propertyUri = propertyUri;
	}

	public void setPropertyUri(String propertyUri) {
		this.propertyUri = propertyUri;
	}

	public String getPropertyUri() {
		return propertyUri;
	}

	public void setProperty(OntoProperty property) {
		this.property = property;		
	}
	
	public OntoProperty getProperty() {
		return property;
	}


}
