package ontoplay.models.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyDTO extends OwlElementDTO {

    private final Double relevance;
    private int domainSize;


    public PropertyDTO(String namespace, String localName, int domainSize, Double relevance) {
        super(namespace, localName);

        this.domainSize = domainSize;
        this.relevance = relevance;
    }

    public int getDomainSize() {
        return domainSize;
    }

    public Double getRelevance() {
        return relevance;
    }
}
