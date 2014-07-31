package controllers;

import java.util.HashMap;

import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;

public class SimpleDatatypePropertyValueRenderer implements
		PropertyValueRenderer {

	public void renderOperator(int conditionId, OntoClass owlClass,
			OntoProperty property, String operator, Renderer renderer) {
		renderer.renderTemplate("Constraints.simpleDatatypeValue", new HashMap<String, Object>());
	}

}
