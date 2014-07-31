package models;

import models.ontologyModel.OntoProperty;


public interface PropertyProvider {

	public abstract OntoProperty getProperty(String propertyUri);

}