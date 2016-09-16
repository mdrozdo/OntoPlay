package ontoplay.models.ontologyReading.owlApi.propertyFactories;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.properties.DateTimeProperty;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

public class DateTimePropertyFactory extends SimpleDatatypePropertyFactory {
	public DateTimePropertyFactory(){
		super("http://www.w3.org/2001/XMLSchema#dateTime");
	}

	@Override
	public OntoProperty createProperty(OWLOntology onto, OWLProperty property) {
		return new DateTimeProperty(property.getIRI().getStart(), property.getIRI().getFragment(),"");
	}

}
