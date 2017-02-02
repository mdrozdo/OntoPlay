package ontoplay.controllers.webservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ontoplay.OntologyHelper;
import ontoplay.models.owlGeneration.OntologyGenerator;
import org.apache.commons.lang.NullArgumentException;
import org.semanticweb.owlapi.model.OWLOntology;

import com.google.gson.GsonBuilder;
import com.hp.hpl.jena.ontology.AnnotationProperty;

import ontoplay.controllers.OntologyController;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.ClassCondition;
import ontoplay.models.ConditionDeserializer;
import ontoplay.models.angular.IndividualDTO;
import ontoplay.models.angular.update.AnnotationAxiom;
import ontoplay.models.angular.update.IndividualUpdateModel;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.OntologyReader;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;

import javax.inject.Inject;

public class Individuals extends OntologyController {

	private final OntologyReader ontologyReader;
	private OntologyGenerator ontologyGenerator;

	@Inject
	public Individuals(OntologyHelper ontologyHelper, OntologyReader ontologyReader, OntologyGenerator generator){
		super(ontologyHelper);
		this.ontologyReader = ontologyReader;

		this.ontologyGenerator = generator;
	}

	public Result getIndividualsByClassName(String className) {
		try {
			OntoClass owlClass = ontoHelper.getOwlClass(className);

			//TODO: What was this one?
			//setObjects();
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

	public Result addIndividual() {
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

			ontoHelper.checkOntology(generatedOntology);
			OntologyReader checkOntologyReader = ontoHelper.checkOwlReader();
			OntologyUtils.saveOntology(generatedOntology);
			// Fix nested individuals
			return ok("ok");
		} catch (Exception e) {
			System.out.println("Exception ocurred when adding individual: " + e.getMessage());
			return ok(e.getMessage());
		}
	}

	public Result getIndividualData(String individualName) {
		try {
			if(!individualName.contains("#")){
				individualName=OntologyUtils.nameSpace + individualName;
			}
			OwlIndividual individual = ontologyReader.getIndividual(individualName);

			if (individual == null || individual.getIndividual() == null) {
				return ok("Individual Not Found");
			}
			Set<AnnotationProperty> annotationProperties = ontologyReader.getAnnotations();
			
			IndividualUpdateModel ind = new IndividualUpdateModel(individual.getIndividual(), ontologyReader, annotationProperties,
					AnnotationAxiom.readIterator(ontologyReader.getAnnotationsAxioms()));

			return ok(new GsonBuilder().create().toJson(ind.getUpdateIndividual()));
		} catch (NullArgumentException e) {
			return ok("Individual Not Found");
		} catch (Exception e) {
			return ok("Individual Not Found " + e.toString());
		}

	}
	
	public Result deleteIndividualByName(String individualName){
		if(!individualName.contains("#")){
			individualName=OntologyUtils.nameSpace + individualName;
		}
		return ok(ontologyGenerator.removeIndividual(individualName)+"");
	}

}
