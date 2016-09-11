package ontoplay.models;

import ontoplay.models.ontologyModel.OntoProperty;


public interface PropertyProvider {

	public abstract OntoProperty getProperty(String propertyUri) throws ConfigurationException;

}