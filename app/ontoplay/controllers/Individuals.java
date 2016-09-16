package ontoplay.controllers;

import java.util.Map;

import ontoplay.OntologyHelper;
import ontoplay.jobs.JenaOwlReaderConfiguration;
import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;
import play.mvc.*;

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
		new JenaOwlReaderConfiguration().initialize(OntologyHelper.file,new JenaOwlReaderConfig().useLocalMapping(OntologyHelper.iriString,OntologyHelper.fileName));
		OntoClass owlClass = ontologyReader.getOwlClass(classUri);
		
		
		OntoProperty property = ontologyReader.getProperty(propertyUri);
		
		//from here I can know the property type (data,object,string ,date);
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		
		final HtmlHolder holder = new HtmlHolder();
		conditionRenderer.renderProperty(conditionId, owlClass, property, true,
				new Renderer() {

					@Override
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
