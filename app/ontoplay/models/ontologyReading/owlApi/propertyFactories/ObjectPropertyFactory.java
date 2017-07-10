package ontoplay.models.ontologyReading.owlApi.propertyFactories;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.owlApi.OwlPropertyFactory;
import ontoplay.models.properties.OwlObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

public class ObjectPropertyFactory extends OwlPropertyFactory {
    @Override
    public boolean canCreateProperty(OWLOntology onto, OWLProperty property) {
        return property.isOWLObjectProperty();
    }

    @Override
    public OntoProperty createProperty(OWLOntology onto, OWLProperty property) {
        return new OwlObjectProperty(property.getIRI().getNamespace(), property.getIRI().getFragment(), "");
    }

}
