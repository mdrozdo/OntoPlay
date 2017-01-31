package ontoplay.controllers.webservices;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import ontoplay.controllers.OntologyController;
import ontoplay.models.angular.PropertyDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import play.mvc.Result;

/**
 * 
 * @author Motasem Alwazir
 *
 */

public class Properties extends OntologyController {

	public static Result getPropertiesByClassName(String className) {
		try {
			OntoClass owlClass = getOwlClass(className);
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
