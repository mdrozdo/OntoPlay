package ontoplay.models.ontologyReading.jena.propertyFactories;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyReading.jena.OwlPropertyFactory;
import ontoplay.models.properties.OwlObjectProperty;
import org.apache.jena.ontology.OntProperty;

import java.util.List;

public class ObjectPropertyFactory extends OwlPropertyFactory {

    @Override
    public boolean canCreateProperty(OntProperty ontProperty) {
        return ontProperty.isObjectProperty();
    }

    @Override
    public OntoProperty createProperty(OntProperty ontProperty, List<OwlElement> domain) {
        return new OwlObjectProperty(ontProperty.getNameSpace(), ontProperty.getLocalName(), ontProperty.getLabel(""), domain.toArray(OwlElement[]::new));
    }

}
