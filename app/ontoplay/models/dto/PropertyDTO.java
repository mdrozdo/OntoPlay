package ontoplay.models.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyDTO extends OwlElementDTO {

    private final Double relevance;
    private List<OwlElementDTO> domain;


    public PropertyDTO(String namespace, String localName, List<OwlElementDTO> domain, Double relevance) {
        super(namespace, localName);

        this.domain = domain;
        this.relevance = relevance;
    }

    public List<OwlElementDTO> getDomain() {
        return domain;
    }

    public Double getRelevance() {
        return relevance;
    }
}
