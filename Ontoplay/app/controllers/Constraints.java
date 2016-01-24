package controllers;

import java.util.Map;

import models.ConfigurationException;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
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
		        controllers.routes.javascript.Constraints.condition(),
		        controllers.routes.javascript.Constraints.getValueCondition(),
		        controllers.routes.javascript.Constraints.getPropertyCondition(),
		        controllers.routes.javascript.Individuals.getPropertyCondition(),
		        controllers.samples.routes.javascript.OntoPlay.add()
		        //controllers.routes.javascript.Application.getPropertyCondition(),
		        //controllers.routes.javascript.Application.getValueCondition(),
        ));
    }

	public static Result condition(int conditionId, String classUri) {
		OntoClass owlClass = ontologyReader.getOwlClass(classUri);
		maxConditionId++;
		int newConditionId = maxConditionId;
		return ok(condition.render( owlClass, ""+newConditionId));
	}
	
	public static Result individual(int conditionId, String classUri) {
		OntoClass owlClass = ontologyReader.getOwlClass(classUri);
		maxConditionId++;
        int newConditionId = maxConditionId;
        return ok(individual.render( owlClass, ""+newConditionId));
        
	}

   public static Result getPropertyCondition(int conditionId, String classUri,
			String propertyUri) throws ConfigurationException {
		
		OntoClass owlClass = ontologyReader.getOwlClass(classUri);
		OntoProperty property = ontologyReader.getProperty(propertyUri);
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		final HtmlHolder holder = new HtmlHolder();
		
		conditionRenderer.renderProperty(conditionId, owlClass, property, false,
				new Renderer() {

					@Override
					public void renderTemplate(String templateName,
							Map<String, Object> args) {
						holder.value = renderTemplateByName(templateName, args.values().toArray());
					}
				});
		return ok(holder.value);
	}

	public static Result getValueCondition(int conditionId, String classUri,
			String propertyUri, String operator) throws ConfigurationException {
		
		OntoClass owlClass = ontologyReader.getOwlClass(classUri);
	
		OntoProperty property = ontologyReader.getProperty(propertyUri);
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		final HtmlHolder holder = new HtmlHolder();
		
		conditionRenderer.renderOperator(conditionId, owlClass, property, operator, 
				new Renderer() {

					@Override
					public void renderTemplate(String templateName,
							Map<String, Object> args) {
						holder.value = renderTemplateByName(templateName, args.values().toArray());
					}
				});
		
		return ok(holder.value);

	}
}
