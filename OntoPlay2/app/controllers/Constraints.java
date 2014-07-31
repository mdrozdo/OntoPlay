package controllers;

import java.util.Map;

import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReader;
import play.*;
import play.Routes;
import play.mvc.*;

import views.html.*;

public class Constraints extends OntologyController {

	private static int maxConditionId = 1;

    public static Result javascriptRoutes()
    {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
		        // Routes
		        //controllers.routes.javascript.Application.condition()//,
		        controllers.routes.javascript.Constraints.individual(),
		        controllers.routes.javascript.Individuals.getPropertyCondition()
		        //controllers.routes.javascript.Application.getPropertyCondition(),
		        //controllers.routes.javascript.Application.getValueCondition(),
        ));
    }

	public static void condition(int conditionId, String classUri) {
		OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
		maxConditionId++;
		int newConditionId = maxConditionId;
		//TODO: render(newConditionId, owlClass);
	}
	
	public static Result individual(int conditionId, String classUri) {
		OntoClass owlClass = getOntologyReader().getOwlClass(classUri);
		maxConditionId++;
        int newConditionId = maxConditionId;
        return ok(individual.render( owlClass, ""+newConditionId));
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
						//TODO: Constraints.renderTemplate(templateName, args);

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
						//TODO: Constraints.renderTemplate(templateName, args);

					}
				});
	}
}
