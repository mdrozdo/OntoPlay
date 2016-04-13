package models.ontologyReading;

import java.util.List;
import java.util.Set;

import models.ConfigurationException;
import models.PropertyProvider;
import models.angular.AnnotationDTO;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import models.ontologyModel.OwlIndividual;

public abstract class OntologyReader implements PropertyProvider{
	
	private static OntologyReader instance;
	
	public static void setGlobalInstance(OntologyReader reader) {
		instance = reader;
	}

	public static OntologyReader getGlobalInstance() {
		return instance;
	}
	
	public abstract OntoClass getOwlClass(String className);

	@Override
	public abstract OntoProperty getProperty(String propertyUri) throws ConfigurationException;

	public abstract List<OwlIndividual> getIndividualsInRange(OntoClass owlClass, OntoProperty property);

	public abstract List<OntoClass> getClassesInRange( OntoProperty property);
	
	public abstract List<OwlIndividual> getIndividuals(OntoClass owlClass);
	
	public abstract OwlIndividual getIndividual(String name);
	public abstract Set<AnnotationDTO> getAnnotations(boolean isFromNameSpace);

}