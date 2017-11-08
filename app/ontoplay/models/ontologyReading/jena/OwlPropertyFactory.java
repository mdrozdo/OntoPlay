package ontoplay.models.ontologyReading.jena;

import ontoplay.models.ontologyModel.OntoProperty;
import org.apache.jena.ontology.OntProperty;

import java.util.ArrayList;
import java.util.List;

public class OwlPropertyFactory {
    private final List<OwlPropertyFactory> factories = new ArrayList<OwlPropertyFactory>();

    public void registerPropertyFactory(
            OwlPropertyFactory datatypePropertyFactory) {
        factories.add(datatypePropertyFactory);
    }

    public boolean canCreateProperty(OntProperty ontProperty) {
        for (OwlPropertyFactory fact : factories) {
            if (fact.canCreateProperty(ontProperty)) {
                return true;
            }
        }
        return false;
    }

    public OntoProperty createProperty(OntProperty ontProperty) {
        for (OwlPropertyFactory fact : factories) {
            if (fact.canCreateProperty(ontProperty))
                return fact.createProperty(ontProperty);
        }
        return null;
    }
}
