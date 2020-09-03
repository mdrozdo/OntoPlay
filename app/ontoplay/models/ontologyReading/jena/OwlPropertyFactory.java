package ontoplay.models.ontologyReading.jena;

import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;

import java.util.ArrayList;
import java.util.List;

public class OwlPropertyFactory {
    private final List<OwlPropertyFactory> factories = new ArrayList<OwlPropertyFactory>();

    protected OwlElement[] getPropertyDomain(OntProperty property){
        return property.listDeclaringClasses()
                //Only keep named classes. The remaining ones are unions/intersections/class expressions. If these
                // can be reduced to named classes, listDeclaringClasses will return them. Otherwise, we lose some
                // information, but we can still use the domain size as a metric of how generic the property is.
                .filterKeep(res -> !res.isAnon())
                .mapWith(res -> createOwlClass(res))
                .toList()
                .toArray(new OwlElement[0]);
    }

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

    public OntoProperty createProperty(OntProperty ontProperty) {
        for (OwlPropertyFactory fact : factories) {
            if (fact.canCreateProperty(ontProperty))
                return fact.createProperty(ontProperty);
        }
        return null;
    }
}
