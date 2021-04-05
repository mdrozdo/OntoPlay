package ontoplay.controllers.webservices;

import com.google.gson.GsonBuilder;
import ontoplay.controllers.OntologyController;
import ontoplay.controllers.PropertyConditionRenderer;
import ontoplay.controllers.SimpleDatatypePropertyValueRenderer;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.ConfigurationException;
import ontoplay.models.Constants;
import ontoplay.models.PropertyOperator;
import ontoplay.models.dto.OperatorDTO;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.owlGeneration.PropertyConditionRendererProvider;
import play.Logger.ALogger;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Operators extends OntologyController {

    private final PropertyConditionRendererProvider conditionRendererProvider;

    @Inject
    public Operators(OntologyUtils ontoHelper, PropertyConditionRendererProvider conditionRendererProvider) {
        super(ontoHelper);
        this.conditionRendererProvider = conditionRendererProvider;
    }

    //TODO: Move this logic to the client side, PropertyDTO should have datatype as a parameter
    private static String getInputType(Class<? extends OntoProperty> propertyCLass) {
        String classAsString = (propertyCLass + "").toLowerCase();
        if (classAsString.indexOf("object") > -1)
            return "object";
        else if (classAsString.indexOf("float") > -1 || classAsString.indexOf("integer") > -1)
            return "number";
        else if (classAsString.indexOf("time") > -1)
            return "time";
        else if (classAsString.indexOf("datetime") > -1)
            return "date";
        else if (classAsString.indexOf("boolean") > -1)
            return "checkbox";
        else
            return "text";
    }

    public Result getOperators(String propertyUri, boolean isDescriptionOfIndividual) {
        try {
            propertyUri = java.net.URLDecoder.decode(propertyUri, "UTF-8");
            ALogger log = play.Logger.of("application");

            OperatorDTO operatorDTO;

            if(propertyUri.equalsIgnoreCase(Constants.HAS_LOCAL_NAME_URI)){
                operatorDTO = new OperatorDTO("text", List.of(new PropertyOperator("equalTo", "is equal to ", true)));
            } else {
                OntoProperty property = ontologyUtils.getProperty(propertyUri);
                log.info("Getting operator for " + propertyUri + " of the class " + property.getClass());


                PropertyConditionRenderer conditionRenderer = conditionRendererProvider
                        .getRenderer(property);

                operatorDTO = new OperatorDTO(getInputType(property.getClass()),
                        conditionRenderer.getOperators(isDescriptionOfIndividual));
            }


            return ok(new GsonBuilder().create().toJson(operatorDTO));

        } catch (ConfigurationException e) {
            return badRequest();
        } catch (UnsupportedEncodingException e) {
            return badRequest();
        }
    }
}
