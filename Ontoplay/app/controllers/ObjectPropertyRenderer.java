package controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import models.ontologyReading.jena.JenaOwlReader;

public class ObjectPropertyRenderer extends PropertyConditionRenderer {

	@Override
	public void renderProperty(int conditionId, OntoClass owlClass, OntoProperty prop, boolean isDescriptionOfIndividual, 
			Renderer renderer) {
		Map<String, Object> args = new LinkedHashMap<String, Object>();
		args.put("conditionId", conditionId);
		args.put("classUri", owlClass.getUri());
		args.put("propertyUri", prop.getUri());
		args.put("isDescriptionOfIndividual", isDescriptionOfIndividual);

		renderer.renderTemplate("Constraints.objectCondition", args);
	}

	@Override
	public void renderOperator(int conditionId, OntoClass owlClass,
			OntoProperty property, String operator, Renderer renderer) {
		if (operator.equals("equalToIndividual")) {
			renderIndividualValueCondition(conditionId, owlClass, property, operator, renderer);
		} else if (operator.equals("constrainedBy")) {
			renderConstrainedValueCondition(conditionId, owlClass, property, operator, false, renderer);
		} else if (operator.equals("describedWith")){
			renderConstrainedValueCondition(conditionId, owlClass, property, operator, true, renderer);
		}

	}

	private void renderConstrainedValueCondition(int conditionId,
			OntoClass owlClass, OntoProperty property, String operator,
			boolean isDescriptionOfIndividual, Renderer renderer) {
		
		Map<String, Object> args = new LinkedHashMap<String, Object>();
		args.put("conditionId", conditionId);
		args.put("classes", JenaOwlReader.getGlobalInstance().getClassesInRange(owlClass, property));
		args.put("isDescriptionOfIndividual", isDescriptionOfIndividual);
		
		renderer.renderTemplate("Constraints.constrainedValueCondition", args);
	}

	private void renderIndividualValueCondition(int conditionId,
			OntoClass owlClass, OntoProperty property, String operator,
			Renderer renderer) {

		Map<String, Object> args = new LinkedHashMap<String, Object>();
		args.put("individuals", JenaOwlReader.getGlobalInstance().getIndividualsInRange(owlClass, property));
		
		renderer.renderTemplate("Constraints.individualValueCondition", args);
		
	}
}
