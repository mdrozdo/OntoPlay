package ontoplay.models.ontologyReading.jena;

import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OwlPropertyFactory {
    private final List<OwlPropertyFactory> factories = new ArrayList<OwlPropertyFactory>();


    private OwlElement createOwlClass(OntClass ontClass){
        return new OntoClass((OntClass) ontClass);
    }

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

    public OntoProperty createProperty(OntProperty ontProperty, List<OwlElement> domain) {
        for (OwlPropertyFactory fact : factories) {
            if (fact.canCreateProperty(ontProperty))
                return fact.createProperty(ontProperty, domain);
        }
        return null;
    }
}
