package ontoplay.controllers;

import java.util.Map;

import ontoplay.OntologyHelper;
import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.owlGeneration.PropertyConditionRendererProvider;
import play.mvc.*;

import javax.inject.Inject;

public class Individuals extends OntologyController {

	private static int maxConditionId = 1;
	private PropertyConditionRendererProvider conditionRendererProvider;

	@Inject
	public Individuals(OntologyHelper ontologyHelper, PropertyConditionRendererProvider conditionRendererProvider){
		super(ontologyHelper);
		this.conditionRendererProvider = conditionRendererProvider;
	}

	//TODO: Is this method used?
	// public static void individual(int conditionId, String classUri) {
	// 	OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
	// 	maxConditionId++;
	// 	int newConditionId = maxConditionId;
	// 	//TODO: render(newConditionId, owlClass);
	// }

	public Result getPropertyCondition(int conditionId, String classUri,
			String propertyUri) throws ConfigurationException {

		OntoClass owlClass = ontoHelper.getOwlClass(classUri);

		OntoProperty property = ontoHelper.getProperty(propertyUri);
		
		//from here I can know the property type (data,object,string ,date);
		PropertyConditionRenderer conditionRenderer = conditionRendererProvider
				.getRenderer(property);
		
		
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
