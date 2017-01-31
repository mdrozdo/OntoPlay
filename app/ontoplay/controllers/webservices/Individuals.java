package ontoplay.controllers.webservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NullArgumentException;
import org.semanticweb.owlapi.model.OWLOntology;

import com.google.gson.GsonBuilder;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import ontoplay.controllers.OntologyController;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.ClassCondition;
import ontoplay.models.ConditionDeserializer;
import ontoplay.models.angular.AnnotationDTO;
import ontoplay.models.angular.IndividualDTO;
import ontoplay.models.angular.update.AnnotationAxiom;
import ontoplay.models.angular.update.IndividualUpdateModel;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.OntologyReader;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;

public class Individuals extends OntologyController {

	public static Result getIndividualsByClassName(String className) {
		try {
			OntoClass owlClass = getOwlClass(className);
			setObjects();
			List<OwlIndividual> individuals = ontologyReader.getIndividuals(owlClass);

			List<IndividualDTO> individualDTOs = new ArrayList<IndividualDTO>();

			for (OwlIndividual owlIndividual : individuals) {
				individualDTOs.add(new IndividualDTO(owlIndividual));
			}
			return ok(new GsonBuilder().create().toJson(individualDTOs));
		} catch (Exception e) {
			System.out.println("Error getting class individuals " + e.toString());
			return badRequest();
		}
	}

	public static Result addIndividual() {
		@SuppressWarnings("deprecation")
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		String conditionJson = dynamicForm.get("conditionJson");
		String individualName = dynamicForm.get("name");

		try {
			ClassCondition condition = ConditionDeserializer.deserializeCondition(ontologyReader, conditionJson);
			OWLOntology generatedOntology = ontologyGenerator
					.convertToOwlIndividualOntology(OntologyUtils.nameSpace + individualName, condition);
			try {
				OwlIndividual individual = ontologyReader.getIndividual(OntologyUtils.nameSpace + individualName);
				if (individual != null)
					return ok("Indvidual name is already used");
			} catch (Exception e) {
			}

			if (generatedOntology == null)
				return ok("Ontology is null");

			OntologyUtils.checkOntology(generatedOntology);
			OntologyReader checkOntologyReader = OntologyUtils.checkOwlReader();
			OntologyUtils.saveOntology(generatedOntology);
			// Fix nested individuals
			return ok("ok");
		} catch (Exception e) {
			System.out.println("Exception ocurred when adding individual: " + e.getMessage());
			return ok(e.getMessage());
		}
	}

	public static Result getIndividualData(String individualName) {
		try {
			if(!individualName.contains("#")){
				individualName=OntologyUtils.nameSpace + individualName;
			}
			OwlIndividual individual = OntologyReader.getGlobalInstance()

					.getIndividual(individualName);

			if (individual == null || individual.getIndividual() == null) {
				return ok("Individual Not Found");
			}
			Set<AnnotationProperty> annotationProperties = OntologyReader.getGlobalInstance().getAnnotations();
			
			IndividualUpdateModel ind = new IndividualUpdateModel(individual.getIndividual(), annotationProperties,
					AnnotationAxiom.readIterator(OntologyReader.getGlobalInstance().getAnnotationsAxioms()));

			return ok(new GsonBuilder().create().toJson(ind.getUpdateIndividual()));
		} catch (NullArgumentException e) {
			return ok("Individual Not Found");
		} catch (Exception e) {
			return ok("Individual Not Found " + e.toString());
		}

	}
	
	public static Result deleteIndividualByName(String individualName){
		if(!individualName.contains("#")){
			individualName=OntologyUtils.nameSpace + individualName;
		}
		return ok(ontologyGenerator.removeIndividual(individualName)+"");
	}

}
