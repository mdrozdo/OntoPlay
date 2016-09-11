package ontoplay.models.ontologyReading.owlApi.propertyFactories;


import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.properties.StringProperty;

public class StringPropertyFactory extends SimpleDatatypePropertyFactory {
	public StringPropertyFactory(){
		super("http://www.w3.org/2001/XMLSchema#string", "http://www.w3.org/2001/XMLSchema#boolean");
	}
	
	@Override
	public OntoProperty createProperty(OWLOntology onto, OWLProperty property) {
		return new StringProperty(property.getIRI().getStart(), property.getIRI().getFragment(),"");
	}

}
