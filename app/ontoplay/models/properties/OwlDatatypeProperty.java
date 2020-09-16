package ontoplay.models.properties;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OwlDatatypeProperty extends OntoProperty {
    private final String datatype;

    public OwlDatatypeProperty(String namespace, String localName, String datatype, String label, OwlElement... domainClasses) {
        super(namespace, localName, label, domainClasses);
        this.datatype = datatype;
    }

    public String getDatatype() {
        return datatype;
    }
}
