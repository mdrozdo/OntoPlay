package models.ontologyReading;

import java.util.List;

import models.ConfigurationException;
import models.PropertyProvider;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import models.ontologyModel.OwlIndividual;
import models.ontologyReading.jena.JenaOwlReader;

public abstract class OntologyReader implements PropertyProvider{
	
	private static OntologyReader instance;
	
	public static void setGlobalInstance(OntologyReader reader) {
		instance = reader;
	}

	public static OntologyReader getGlobalInstance() {
		return instance;
	}
	
	public abstract OntoClass getOwlClass(String className);

	public abstract OntoProperty getProperty(String propertyUri) throws ConfigurationException;

	public abstract List<OwlIndividual> getIndividualsInRange(OntoClass owlClass, OntoProperty property);

	public abstract List<OntoClass> getClassesInRange(OntoClass owlClass, OntoProperty property);
}