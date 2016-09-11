package ontoplay.models.ontologyReading.jena;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntProperty;

import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.ontologyModel.OntoProperty;

public abstract class OwlPropertyFactory {
	private static List<OwlPropertyFactory> factories = new ArrayList<OwlPropertyFactory>();
	public static void registerPropertyFactory(
			OwlPropertyFactory datatypePropertyFactory) {
		factories.add(datatypePropertyFactory);		
	}
	
	public static OntoProperty createOwlProperty(OntProperty ontProperty){
		for(OwlPropertyFactory fact : factories){
			if(fact.canCreateProperty(ontProperty))
				return fact.createProperty(ontProperty);
		}
			return null;
	}
	
	public abstract boolean canCreateProperty(OntProperty ontProperty);
	public abstract OntoProperty createProperty(OntProperty ontProperty);	
}
