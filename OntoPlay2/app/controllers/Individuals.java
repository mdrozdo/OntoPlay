package controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import models.ConfigurationException;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReader;
import play.*;
import play.mvc.*;

import views.html.*;

public class Individuals extends OntologyController {

	private static int maxConditionId = 1;

	//TODO: Is this method used?
	// public static void individual(int conditionId, String classUri) {
	// 	OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
	// 	maxConditionId++;
	// 	int newConditionId = maxConditionId;
	// 	//TODO: render(newConditionId, owlClass);
	// }

	public static Result getPropertyCondition(int conditionId, String classUri,
			String propertyUri) throws ConfigurationException {
		
		OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
		OntoProperty property = getOntologyReader().getProperty(propertyUri);
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		final HtmlHolder holder = new HtmlHolder();
		conditionRenderer.renderProperty(conditionId, owlClass, property, true,
				new Renderer() {

					public void renderTemplate(String templateName,
							Map<String, Object> args) {
						holder.value = renderTemplateByName(templateName, args.values().toArray());						
					}
				});
		return ok(holder.value);
	}

	//TODO: Is this method used?
// 	public static void getValueCondition(int conditionId, String classUri,
// 			String propertyUri, String operator) throws ConfigurationException {
// 		OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
// 		OntoProperty property = getOntologyReader().getProperty(propertyUri);
		
// 		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
// 				.getRenderer(property.getClass());
		
// 		conditionRenderer.renderOperator(conditionId, owlClass, property, operator, 
// 				new Renderer() {

// 					public void renderTemplate(String templateName,
// 							Map<String, Object> args) {
// 						//TODO: Individuals.renderTemplate(templateName, args);

// 					}
// 				});


// //		renderText(String.format("%d; %s; %s; %s", conditionId, classUri,
// //				propertyUri, operator));
// 	}
}
