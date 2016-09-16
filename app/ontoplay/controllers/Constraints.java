package ontoplay.controllers;

import java.util.Map;

import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import play.Routes;
import play.mvc.*;

import ontoplay.views.html.*;

public class Constraints extends OntologyController {

	private static int maxConditionId = 1;

    public static Result javascriptRoutes()
    {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutesOntoPlay",
		        // Routes
		        //controllers.routes.javascript.Application.condition()//,
		        ontoplay.controllers.routes.javascript.Constraints.individual(),
		        ontoplay.controllers.routes.javascript.Constraints.condition(),
		        ontoplay.controllers.routes.javascript.Constraints.getValueCondition(),
		        ontoplay.controllers.routes.javascript.Constraints.getPropertyCondition(),
		        ontoplay.controllers.routes.javascript.Individuals.getPropertyCondition()
		        //ontoplay.controllers.samples.routes.javascript.OntoPlay.add()
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
