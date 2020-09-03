package ontoplay.models.properties;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OwlDatatypeProperty implements OntoProperty {
    private final String namespace;
    private final String localName;
    private final String datatype;
    private final String label;
    private final List<OwlElement> domain;

    public OwlDatatypeProperty(String namespace, String localName, String datatype, String label, OwlElement... domainClasses) {
        this.namespace = namespace;
        this.localName = localName;
        this.datatype = datatype;
        this.label = label;
        this.domain = Arrays.asList(domainClasses);
    }

    @Override
    public String getLabel() {
        return label;
    }

    public String getDatatype() {
        return datatype;
    }

    @Override
    public String getLocalName() {
        return localName;
    }

    @Override
    public String getUri() {
        return String.format("%s%s", namespace, localName);
    }

    @Override
    public List<OwlElement> getDomain() { return domain; }
}
