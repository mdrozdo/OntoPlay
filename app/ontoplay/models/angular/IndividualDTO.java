package ontoplay.models.angular;

import ontoplay.models.ontologyModel.OwlIndividual;

public class IndividualDTO extends OwlElementDTO {

    public IndividualDTO(OwlIndividual owlIndividual) {
        super(owlIndividual.getUri(), owlIndividual.getLocalName());
    }

}
