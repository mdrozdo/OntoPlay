package ontoplay.models.angular;

import ontoplay.models.ontologyModel.OntoClass;

public class ClassDTO extends OwlElementDTO {

	private String label;

	public ClassDTO(OntoClass owlClass) {
		super(owlClass.getUri(), owlClass.getLocalName());

		this.label = owlClass.getLabel();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
