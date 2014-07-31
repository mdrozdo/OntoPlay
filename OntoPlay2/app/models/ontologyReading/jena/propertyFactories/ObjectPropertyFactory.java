package models.ontologyReading.jena.propertyFactories;

import models.ontologyModel.OntoProperty;
import models.ontologyReading.jena.OwlPropertyFactory;
import models.properties.OwlObjectProperty;

import com.hp.hpl.jena.ontology.OntProperty;

public class ObjectPropertyFactory extends OwlPropertyFactory {

	@Override
	public boolean canCreateProperty(OntProperty ontProperty) {
		return ontProperty.isObjectProperty();
	}

	@Override
	public OntoProperty createProperty(OntProperty ontProperty) {
		return new OwlObjectProperty(ontProperty.getNameSpace(), ontProperty.getLocalName());
	}

}
