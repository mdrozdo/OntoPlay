package ontoplay.models.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyDTO extends OwlElementDTO {

    private List<OwlElementDTO> domain;

    public PropertyDTO(String namespace, String localName, List<OwlElementDTO> domain) {
        super(namespace, localName);

        this.domain = domain;
    }

    public List<OwlElementDTO> getDomain() {
        return domain;
    }
}
