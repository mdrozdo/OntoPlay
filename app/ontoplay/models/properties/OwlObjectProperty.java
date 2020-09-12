package ontoplay.models.properties;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;

import java.util.Arrays;
import java.util.List;

public class OwlObjectProperty extends OntoProperty {

    public OwlObjectProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        super(namespace, localName, label, domainClasses);
    }

}
