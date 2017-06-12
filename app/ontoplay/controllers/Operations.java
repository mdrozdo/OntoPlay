package ontoplay.controllers;

import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.ontologyModel.OntoClass;
import play.mvc.Result;

import javax.inject.Inject;


public class Operations extends OntologyController {

	@Inject
	public Operations(OntologyUtils ontologyUtils) {
		super(ontologyUtils);
	}

	public Result add(String className) {

		try {
			OntoClass owlClass = ontologyUtils.getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.addIndividual.render("Add new individual for " + className, className));

		} catch (Exception e) {
			return ok("Can't find the required class:/n+" + e.toString());
		}
	}

	public Result addClassExpression(String className) {

		try {
			OntoClass owlClass = ontologyUtils.getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.addClassExpression.render("Add new class expression for " + className, className));

		} catch (Exception e) {
			return ok("Can't find the required class:/n+" + e.toString());
		}
	}

	public Result addClassMapping(String className) {

		try {
			OntoClass owlClass = ontologyUtils.getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.addClassMapping.render("Add new class mapping for " + className, className));

		} catch (Exception e) {
			return ok("Can't find the required class:/n+" + e.toString());
		}
	}

	public Result update(String className, String individualName) {

		try {
			OntoClass owlClass = ontologyUtils.getOwlClass(className);

			if (owlClass == null) {
				return ok("Class Not Found");
			}

			return ok(ontoplay.views.html.addIndividual.render("update " + individualName, className));

		} catch (Exception e) {
			return ok("Can't find the required classs:/n+" + e.toString());
		}
	}
}