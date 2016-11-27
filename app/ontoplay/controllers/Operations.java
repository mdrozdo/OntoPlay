package ontoplay.controllers;

import ontoplay.models.ontologyModel.OntoClass;
import play.mvc.Result;


public class Operations extends OntologyController {
	
public static Result process(String className){
		
		try {
			OntoClass owlClass = getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.add.render(className));
			
		} catch (Exception e) {
			return ok("Can't find the required classs:/n+" + e.toString());
		}
	}
}