package ontoplay.models.ontologyReading.jena.propertyFactories;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyReading.jena.OwlPropertyFactory;
import ontoplay.models.properties.IntegerProperty;
import org.apache.jena.ontology.OntProperty;

import java.util.List;

public class IntegerPropertyFactory extends OwlPropertyFactory {

    @Override
    public boolean canCreateProperty(OntProperty ontProperty) {
        if (!ontProperty.isDatatypeProperty())
            return false;
        if (ontProperty.getRange() == null)
            return false;
        if (ontProperty.getRange().getURI() == null)
            return false;

        String rangeUri = ontProperty.getRange().getURI();
        return rangeUri.equalsIgnoreCase("http://www.w3.org/2001/XMLSchema#integer") || rangeUri.equalsIgnoreCase("http://www.w3.org/2001/XMLSchema#int");
    }

    @Override
    public OntoProperty createProperty(OntProperty ontProperty, List<OwlElement> domain) {
        return new IntegerProperty(ontProperty.getNameSpace(), ontProperty.getLocalName(), ontProperty.getRange().getURI(), ontProperty.getLabel(""), domain.toArray(OwlElement[]::new));
    }

}
