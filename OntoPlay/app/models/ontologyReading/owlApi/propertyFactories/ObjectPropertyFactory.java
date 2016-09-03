package models.ontologyReading.owlApi.propertyFactories;

import models.ontologyModel.OntoProperty;
import models.ontologyReading.owlApi.OwlPropertyFactory;
import models.properties.OwlObjectProperty;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

public class ObjectPropertyFactory extends OwlPropertyFactory {
	@Override
	public boolean canCreateProperty(OWLOntology onto, OWLProperty property) {
		return property.isOWLObjectProperty();
	}

	@Override
	public OntoProperty createProperty(OWLOntology onto, OWLProperty property) {
		return new OwlObjectProperty(property.getIRI().getStart(), property.getIRI().getFragment(),"");
	}

}
