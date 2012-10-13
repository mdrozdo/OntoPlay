package controllers;

import java.util.Map;

import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReader;
import play.mvc.Controller;

public class Constraints extends OntologyController {

	private static int maxConditionId = 1;

	public static void condition(int conditionId, String classUri) {
		OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
		maxConditionId++;
		int newConditionId = maxConditionId;
		render(newConditionId, owlClass);
	}
	
	public static void individual(int conditionId, String classUri) {
		OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
		maxConditionId++;
		int newConditionId = maxConditionId;
		render(newConditionId, owlClass);
	}

	public static void getPropertyCondition(int conditionId, String classUri,
			String propertyUri) {
		
		OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
		OntoProperty property = getOntologyReader().getProperty(propertyUri);
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		conditionRenderer.renderProperty(conditionId, owlClass, property, false,
				new Renderer() {

					public void renderTemplate(String templateName,
							Map<String, Object> args) {
						Constraints.renderTemplate(templateName, args);

					}
				});
	}

	public static void getValueCondition(int conditionId, String classUri,
			String propertyUri, String operator) {
		OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
		OntoProperty property = getOntologyReader().getProperty(propertyUri);
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		conditionRenderer.renderOperator(conditionId, owlClass, property, operator, 
				new Renderer() {

					public void renderTemplate(String templateName,
							Map<String, Object> args) {
						Constraints.renderTemplate(templateName, args);

					}
				});


//		renderText(String.format("%d; %s; %s; %s", conditionId, classUri,
//				propertyUri, operator));
	}
}
