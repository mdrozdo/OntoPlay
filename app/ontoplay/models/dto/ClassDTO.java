package ontoplay.models.dto;

import ontoplay.models.ontologyModel.OntoClass;

public class ClassDTO extends OwlElementDTO {

    public ClassDTO(OntoClass owlClass) {
        super(owlClass.getUri(), owlClass.getLocalName());
    }

}
