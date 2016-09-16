package ontoplay.controllers;

import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;

public interface PropertyValueRenderer {
	void renderOperator(int conditionId, OntoClass owlClass,
			OntoProperty property, String operator, Renderer renderer);
}
