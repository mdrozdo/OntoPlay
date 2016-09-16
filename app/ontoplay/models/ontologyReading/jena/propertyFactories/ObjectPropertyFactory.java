package ontoplay.models.ontologyReading.jena.propertyFactories;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.jena.OwlPropertyFactory;
import ontoplay.models.properties.OwlObjectProperty;

import com.hp.hpl.jena.ontology.OntProperty;

public class ObjectPropertyFactory extends OwlPropertyFactory {

	@Override
	public boolean canCreateProperty(OntProperty ontProperty) {
		return ontProperty.isObjectProperty();
	}

	@Override
	public OntoProperty createProperty(OntProperty ontProperty) {
		return new OwlObjectProperty(ontProperty.getNameSpace(), ontProperty.getLocalName(),ontProperty.getLabel(""));
	}

}
