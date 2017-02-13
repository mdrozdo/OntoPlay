package ontoplay.models.ontologyReading;

import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.rdf.model.ResIterator;

import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyProvider;
import ontoplay.models.angular.AnnotationDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlIndividual;

import javax.inject.Singleton;

public interface OntologyReader extends PropertyProvider{
	
	OntoClass getOwlClass(String className);

	@Override
	OntoProperty getProperty(String propertyUri) throws ConfigurationException;

	List<OwlIndividual> getIndividualsInRange(OntoClass owlClass, OntoProperty property);

	List<OntoClass> getClassesInRange(OntoProperty property);
	
	List<OwlIndividual> getIndividuals(OntoClass owlClass);
	
	OwlIndividual getIndividual(String name);
	Set<AnnotationDTO> getAnnotations(boolean isFromNameSpace);
	Set<AnnotationProperty> getAnnotations();

	ResIterator getAnnotationsAxioms();

    String getOntologyNamespace();
}
