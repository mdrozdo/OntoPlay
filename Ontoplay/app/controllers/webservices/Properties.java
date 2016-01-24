package controllers.webservices;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import controllers.OntologyController;
import models.angular.PropertyDTO;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import play.mvc.Result;

public class Properties extends OntologyController{
	
	public static Result getPropertiesByClassName(String className){
		try {
			OntoClass owlClass = getOwlClass(className);
			List<OntoProperty> properties=owlClass.getProperties();
			List<PropertyDTO> propertiesDTO=new ArrayList<PropertyDTO>() ;
			
			for (OntoProperty ontoProperty : properties) {
				propertiesDTO.add(new PropertyDTO(ontoProperty.getUri(), ontoProperty.getLocalName()));
				
			}
			
			return ok(new GsonBuilder().create().toJson(propertiesDTO));
		} catch (Exception e) {
			return badRequest();
		}
	}
}
