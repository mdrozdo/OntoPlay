package models.ontologyReading.jena.propertyFactories;

import models.ontologyModel.OntoProperty;
import models.ontologyReading.jena.OwlPropertyFactory;
import models.properties.DateTimeProperty;
import models.properties.FloatProperty;
import models.properties.IntegerProperty;

import com.hp.hpl.jena.ontology.OntProperty;

public class DateTimePropertyFactory extends OwlPropertyFactory {

	@Override
	public boolean canCreateProperty(OntProperty ontProperty) {
		if(!ontProperty.isDatatypeProperty())
			return false;
		
		String rangeUri = ontProperty.getRange().getURI();
		return rangeUri.equalsIgnoreCase("http://www.w3.org/2001/XMLSchema#datetime");
	}

	@Override
	public OntoProperty createProperty(OntProperty ontProperty) {
		return new DateTimeProperty(ontProperty.getNameSpace(), ontProperty.getLocalName());
	}

}
