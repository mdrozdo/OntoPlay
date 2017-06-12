package ontoplay.models.ontologyReading.owlApi;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.properties.StringProperty;

public abstract class OwlPropertyFactory {
	private static List<OwlPropertyFactory> factories = new ArrayList<OwlPropertyFactory>();
	public static void registerPropertyFactory(
			OwlPropertyFactory datatypePropertyFactory) {
		factories.add(datatypePropertyFactory);		
	}
	
	public static OntoProperty createOwlProperty(OWLOntology onto, OWLProperty property){
		for(OwlPropertyFactory fact : factories){
			if(fact.canCreateProperty(onto, property))
				return fact.createProperty(onto, property);
		}
		return new StringProperty(property.getIRI().getNamespace(), property.getIRI().getFragment(),"");
		//throw new InvalidConfigurationException(String.format("Failed to create a property description object for property: %s. No property factory has been found to handle the property type: %s.", property.getIRI(), property.getRanges(onto)));
	}
	
	public abstract boolean canCreateProperty(OWLOntology onto, OWLProperty property);
	public abstract OntoProperty createProperty(OWLOntology onto, OWLProperty property);	
}
