package ontoplay.controllers;

import ontoplay.OntologyHelper;
import ontoplay.models.ontologyModel.OntoClass;
import play.mvc.Result;

import javax.inject.Inject;


public class Operations extends OntologyController {

	@Inject
	public Operations(OntologyHelper ontologyHelper) {
		super(ontologyHelper);
	}

	public Result add(String className) {

		try {
			OntoClass owlClass = ontoHelper.getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.add.render("Add " + className, className));

		} catch (Exception e) {
			return ok("Can't find the required classs:/n+" + e.toString());
		}
	}

	public Result addClassExpression(String className) {

		try {
			OntoClass owlClass = ontoHelper.getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.add.render("Add " + className, className));

		} catch (Exception e) {
			return ok("Can't find the required classs:/n+" + e.toString());
		}
	}

	public Result update(String className, String individualName) {

		try {
			OntoClass owlClass = ontoHelper.getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.add.render("update " + individualName, className));

		} catch (Exception e) {
			return ok("Can't find the required classs:/n+" + e.toString());
		}
	}
}