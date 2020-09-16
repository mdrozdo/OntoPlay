package ontoplay.models.ontologyModel;


import java.util.Arrays;
import java.util.List;

public class OntoProperty implements OwlElement {
    private final String namespace;
    private final String localName;
    private final String label;
    private final List<OwlElement> domain;

    private Double relevance;

    public OntoProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        this.namespace = namespace;
        this.localName = localName;
        this.label = label;
        this.domain = Arrays.asList(domainClasses);
    }

    public String getLocalName() {
        return localName;
    }

    public String getLabel() {
        return label;
    }

    public String getUri() {
        return String.format("%s%s", namespace, localName);
    }

    public List<OwlElement> getDomain(){
        return domain;
    }

    public Double getRelevance(){
        return relevance;
    }

    public void setRelevance(Double relevance) {
        this.relevance = relevance;
    }

}
