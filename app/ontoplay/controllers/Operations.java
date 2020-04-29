package ontoplay.controllers;

import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.ontologyModel.OntoClass;
import play.mvc.Result;

import javax.inject.Inject;


public class Operations extends OntologyController {

    private final MainTemplate mainTemplate;

    @Inject
    public Operations(OntologyUtils ontologyUtils, MainTemplate mainTemplate) {
        super(ontologyUtils);
        this.mainTemplate = mainTemplate;
    }

    public Result add(String className) {

        try {
            OntoClass owlClass = ontologyUtils.getOwlClass(className);

            if (owlClass == null) {
                return ok("Class Not Found");
            }

            return ok(ontoplay.views.html.addIndividual.render("Add new individual for " + className, owlClass.getUri(), mainTemplate.getRenderFunction()));

        } catch (Exception e) {
            return ok("Can't find the required class:/n/n+" + e.toString());
        }
    }

    public Result addReact(String className) {

        try {
            OntoClass owlClass = ontologyUtils.getOwlClass(className);

            if (owlClass == null) {
                return ok("Class Not Found");
            }

            return ok(ontoplay.views.html.addIndividualReact.render("Add new individual", owlClass.getUri(), mainTemplate.getRenderFunction()));

        } catch (Exception e) {
            return ok("Can't find the required class:/n/n+" + e.toString());
        }
    }

    public Result addClassExpression(String className) {

        try {
            OntoClass owlClass = ontologyUtils.getOwlClass(className);

            if (owlClass == null) {
                return ok("Class Not Found");
            }

            return ok(ontoplay.views.html.addClassExpression.render("Add new class expression for " + className, owlClass.getUri(), mainTemplate.getRenderFunction()));

        } catch (Exception e) {
            return ok("Can't find the required class:/n+" + e.toString());
        }
    }

    public Result addClassExpressionReact(String className) {

        try {
            OntoClass owlClass = ontologyUtils.getOwlClass(className);

            if (owlClass == null) {
                return ok("Class Not Found");
            }

            return ok(ontoplay.views.html.addClassExpressionReact.render("Add new class expression", owlClass.getUri(), mainTemplate.getRenderFunction()));

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

            return ok(ontoplay.views.html.addClassMapping.render("Add new class mapping for " + className, owlClass.getUri(), mainTemplate.getRenderFunction()));

        } catch (Exception e) {
            return ok("Can't find the required class:/n+" + e.toString());
        }
    }

    public Result addClassMappingReact(String className) {

        try {
            OntoClass owlClass = ontologyUtils.getOwlClass(className);

            if (owlClass == null) {
                return ok("Class Not Found");
            }

            return ok(ontoplay.views.html.addClassMappingReact.render("Add new class mapping", owlClass.getUri(), mainTemplate.getRenderFunction()));

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

            return ok(ontoplay.views.html.addIndividual.render("update " + individualName, owlClass.getUri(), mainTemplate.getRenderFunction()));

        } catch (Exception e) {
            return ok("Can't find the required classs:/n+" + e.toString());
        }
    }
}