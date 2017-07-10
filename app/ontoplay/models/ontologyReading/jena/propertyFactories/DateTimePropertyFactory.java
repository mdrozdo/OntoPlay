package ontoplay.models.ontologyReading.jena.propertyFactories;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.jena.OwlPropertyFactory;
import ontoplay.models.properties.DateTimeProperty;
import org.apache.jena.ontology.OntProperty;

public class DateTimePropertyFactory extends OwlPropertyFactory {

    @Override
    public boolean canCreateProperty(OntProperty ontProperty) {

        if (!ontProperty.isDatatypeProperty())
            return false;
        if (ontProperty.getRange() == null)
            return false;
        if (ontProperty.getRange().getURI() == null)
            return false;

        String rangeUri = ontProperty.getRange().getURI();
        return rangeUri.equalsIgnoreCase("http://www.w3.org/2001/XMLSchema#dateTime");
    }

    @Override
    public OntoProperty createProperty(OntProperty ontProperty) {
        return new DateTimeProperty(ontProperty.getNameSpace(), ontProperty.getLocalName(), ontProperty.getLabel(""));
    }

}
