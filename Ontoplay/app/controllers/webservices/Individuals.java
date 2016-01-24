package controllers.webservices;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import controllers.OntologyController;
import models.angular.IndividualDTO;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OwlIndividual;
import play.mvc.Result;

public class Individuals extends OntologyController {

	public static Result getIndividualsByClassName(String className) {
		try {
			OntoClass owlClass = getOwlClass(className);
			List<OwlIndividual> individuals = ontologyReader.getIndividuals(owlClass);

			List<IndividualDTO> individualDTOs = new ArrayList<IndividualDTO>();

			for (OwlIndividual owlIndividual : individuals) {
				individualDTOs.add(new IndividualDTO(owlIndividual));
			}
			return ok(new GsonBuilder().create().toJson(individualDTOs));
		} catch (Exception e) {
			return badRequest();
		}
	}

}
