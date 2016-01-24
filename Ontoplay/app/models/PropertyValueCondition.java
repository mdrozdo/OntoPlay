package models;

import models.ontologyModel.OntoProperty;


public class PropertyValueCondition<T extends OntoProperty> {
	private String propertyUri;
	private T property;

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

	public void setProperty(T property) {
		this.property = property;		
	}
	
	public T getProperty() {
		return property;
	}


}
