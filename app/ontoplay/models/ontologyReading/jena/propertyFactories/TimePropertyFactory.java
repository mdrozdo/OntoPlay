package ontoplay.models.ontologyReading.jena.propertyFactories;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyReading.jena.OwlPropertyFactory;
import ontoplay.models.properties.DateTimeProperty;
import ontoplay.models.properties.TimeProperty;
import org.apache.jena.ontology.OntProperty;

import java.util.List;

public class TimePropertyFactory extends OwlPropertyFactory {

    @Override
    public boolean canCreateProperty(OntProperty ontProperty) {

        if (!ontProperty.isDatatypeProperty())
            return false;
        if (ontProperty.getRange() == null)
            return false;
        if (ontProperty.getRange().getURI() == null)
            return false;

        String rangeUri = ontProperty.getRange().getURI();
        return rangeUri.equalsIgnoreCase("http://www.w3.org/2001/XMLSchema#time");
    }

    @Override
    public OntoProperty createProperty(OntProperty ontProperty, List<OwlElement> domain) {
        return new TimeProperty(ontProperty.getNameSpace(), ontProperty.getLocalName(), ontProperty.getLabel(""), domain.toArray(OwlElement[]::new));
    }

}