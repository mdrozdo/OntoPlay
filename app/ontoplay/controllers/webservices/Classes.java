package ontoplay.controllers.webservices;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import controllers.OntologyController;
import models.ConfigurationException;
import models.angular.ClassDTO;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import play.Logger.ALogger;
import play.mvc.Result;

public class Classes extends OntologyController{
	public static Result getClassesByProperty(String propertyUri){
		try{
	
		OntoProperty property = ontologyReader.getProperty(propertyUri);
		List<OntoClass> classes=ontologyReader.getClassesInRange(property);
		List<ClassDTO> classesDTO=new ArrayList<ClassDTO>();
		for (OntoClass owlClass : classes) {
			classesDTO.add(new ClassDTO(owlClass));			
		}
		return ok(new GsonBuilder().create().toJson(classesDTO));
		}catch(ConfigurationException e){
			return badRequest();
		}
		}
}
