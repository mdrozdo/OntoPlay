package ontoplay.controllers.webservices;

import com.google.gson.GsonBuilder;
import ontoplay.controllers.OntologyController;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.angular.PropertyDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Motasem Alwazir
 */

public class Properties extends OntologyController {

    @Inject
    public Properties(OntologyUtils ontoHelper) {
        super(ontoHelper);
    }

    public Result getPropertiesByClassName(String className) {
        try {
            OntoClass owlClass = ontologyUtils.getOwlClass(className);
            List<OntoProperty> properties = owlClass.getProperties();
            List<PropertyDTO> propertiesDTO = new ArrayList<PropertyDTO>();

            for (OntoProperty ontoProperty : properties) {

                propertiesDTO.add(new PropertyDTO(ontoProperty.getUri(), ontoProperty.getLocalName()));

            }

            return ok(new GsonBuilder().create().toJson(propertiesDTO));
        } catch (Exception e) {
            return badRequest();
        }
    }

}
