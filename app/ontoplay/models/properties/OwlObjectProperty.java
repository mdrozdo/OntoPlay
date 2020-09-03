package ontoplay.models.properties;

import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;

import java.util.Arrays;
import java.util.List;

public class OwlObjectProperty implements OntoProperty {

    private final String namespace;
    private final String localName;
    private final String label;
    private final List<OwlElement> domain;

    public OwlObjectProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        this.namespace = namespace;
        this.localName = localName;
        this.label = label;
        this.domain = Arrays.asList(domainClasses);
    }

    @Override
    public String getLocalName() {
        return localName;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getUri() {
        return String.format("%s%s", namespace, localName);
    }

    @Override
    public List<OwlElement> getDomain() { return domain; }
}
