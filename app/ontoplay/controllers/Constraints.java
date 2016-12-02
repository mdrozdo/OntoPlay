package ontoplay.controllers;

import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.owlGeneration.PropertyConditionRendererProvider;
import ontoplay.views.html.*;
import play.Routes;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Map;

public class Constraints extends OntologyController {

	private static int maxConditionId = 1;
	private PropertyConditionRendererProvider conditionRendererProvider;

	@Inject
	public Constraints(PropertyConditionRendererProvider conditionRendererProvider){

		this.conditionRendererProvider = conditionRendererProvider;
	}

    public Result javascriptRoutes()
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

	public Result condition(int conditionId, String classUri) {
		OntoClass owlClass = ontoHelper.getOwlClass(classUri);
		maxConditionId++;
		int newConditionId = maxConditionId;
		return ok(condition.render( owlClass, ""+newConditionId));
	}
	
	public Result individual(int conditionId, String classUri) {
		OntoClass owlClass = ontoHelper.getOwlClass(classUri);
		maxConditionId++;
        int newConditionId = maxConditionId;
        return ok(individual.render( owlClass, ""+newConditionId));
        
	}

   public Result getPropertyCondition(int conditionId, String classUri,
			String propertyUri) throws ConfigurationException {
		
		OntoClass owlClass = ontoHelper.getOwlClass(classUri);
		OntoProperty property = ontoHelper.getProperty(propertyUri);
		
		PropertyConditionRenderer conditionRenderer = conditionRendererProvider
				.getRenderer(property);
		
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

	public Result getValueCondition(int conditionId, String classUri,
			String propertyUri, String operator) throws ConfigurationException {
		
		OntoClass owlClass = ontoHelper.getOwlClass(classUri);
	
		OntoProperty property = ontoHelper.getProperty(propertyUri);
		
		PropertyConditionRenderer conditionRenderer = conditionRendererProvider.getRenderer(property);
		
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
