package ontoplay.controllers;

import ontoplay.models.ontologyModel.OntoClass;
import play.mvc.Result;


public class Operations extends OntologyController {
	
public static Result add(String className){
		
		try {
			OntoClass owlClass = getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.add.render("Add "+className,className));
			
		} catch (Exception e) {
			return ok("Can't find the required classs:/n+" + e.toString());
		}
	}

public static Result update(String className,String individualName){
	
	try {
		OntoClass owlClass = getOwlClass(className);

		if (owlClass == null) {
			return ok("Class Not Found");
		}

		return ok(ontoplay.views.html.add.render("update "+individualName,className));
		
	} catch (Exception e) {
		return ok("Can't find the required classs:/n+" + e.toString());
	}
}
}