package controllers;

import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;

public interface PropertyValueRenderer {
	void renderOperator(int conditionId, OntoClass owlClass,
			OntoProperty property, String operator, Renderer renderer);
}
