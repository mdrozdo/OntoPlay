package models.ontologyReading.owlApi.propertyFactories;

import models.ontologyModel.OntoProperty;
import models.properties.FloatProperty;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

public class FloatPropertyFactory extends SimpleDatatypePropertyFactory {
	public FloatPropertyFactory(){
		super("http://www.w3.org/2001/XMLSchema#float");
	}

	@Override
	public OntoProperty createProperty(OWLOntology onto, OWLProperty property) {
		return new FloatProperty(property.getIRI().getStart(), property.getIRI().getFragment(),"");
	}
}
