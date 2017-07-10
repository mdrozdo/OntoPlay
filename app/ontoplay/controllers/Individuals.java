package ontoplay.controllers;

import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.owlGeneration.PropertyConditionRendererProvider;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Map;

public class Individuals extends OntologyController {

    private static int maxConditionId = 1;
    private PropertyConditionRendererProvider conditionRendererProvider;

    @Inject
    public Individuals(OntologyUtils ontologyUtils, PropertyConditionRendererProvider conditionRendererProvider) {
        super(ontologyUtils);
        this.conditionRendererProvider = conditionRendererProvider;
    }

    public Result getPropertyCondition(int conditionId, String classUri,
                                       String propertyUri) throws ConfigurationException {

        OntoClass owlClass = ontologyUtils.getOwlClass(classUri);

        OntoProperty property = ontologyUtils.getProperty(propertyUri);

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
}
