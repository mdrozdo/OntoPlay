package ontoplay.models.ontologyReading;

import java.util.List;
import java.util.Set;

import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyProvider;
import ontoplay.models.angular.AnnotationDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlIndividual;

import javax.inject.Singleton;

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