package ontoplay.controllers.webservices;

import com.google.gson.GsonBuilder;
import org.apache.jena.ontology.AnnotationProperty;
import ontoplay.controllers.utils.OntologyUtils;
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
import ontoplay.models.owlGeneration.OntologyGenerator;
import org.semanticweb.owlapi.model.OWLOntology;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Individuals extends OntologyController {

	private final OntologyReader ontologyReader;
	private OntologyGenerator ontologyGenerator;
	private OntologyUtils utils;

	@Inject
	public Individuals(OntologyUtils ontologyUtils, OntologyReader ontologyReader, OntologyGenerator generator, OntologyUtils utils){
		super(ontologyUtils);
		this.ontologyReader = ontologyReader;

		this.ontologyGenerator = generator;
		this.utils = utils;
	}

	public Result getIndividualsByClassName(String className) {
		try {
			OntoClass owlClass = ontologyUtils.getOwlClass(className);

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
			String individualUri = utils.joinNamespaceAndName(ontologyReader.getOntologyNamespace(), individualName);
			OWLOntology generatedOntology = ontologyGenerator
					.convertToOwlIndividualOntology(individualUri, condition);
			try {
				OwlIndividual individual = ontologyReader.getIndividual(individualUri);
				if (individual != null)
					return ok("Indvidual name is already used");
			} catch (Exception e) {
			}

			if (generatedOntology == null)
				return ok("Ontology is null");

			ontologyUtils.checkOntology(generatedOntology);
			OntologyReader checkOntologyReader = ontologyUtils.checkOwlReader();
			utils.saveOntology(generatedOntology);
			// Fix nested individuals
			return ok("ok");
		} catch (Exception e) {
			System.out.println("Exception ocurred when adding individual: " + e.getMessage());
			return ok(e.getMessage());
		}
	}

	public Result getIndividualData(String individualName) {
		try {
			individualName = utils.nameToUri(individualName, ontologyReader.getOntologyNamespace());
			OwlIndividual individual = ontologyReader.getIndividual(individualName);

			if (individual == null || individual.getIndividual() == null) {
				return ok("Individual Not Found");
			}
			Set<AnnotationProperty> annotationProperties = ontologyReader.getAnnotations();
			
			IndividualUpdateModel ind = new IndividualUpdateModel(individual.getIndividual(), ontologyReader, annotationProperties,
					AnnotationAxiom.readIterator(ontologyReader.getAnnotationsAxioms()));

			return ok(new GsonBuilder().create().toJson(ind.getUpdateIndividual()));
		} catch (IllegalArgumentException e) {
			return ok("Individual Not Found");
		} catch (Exception e) {
			return ok("Individual Not Found " + e.toString());
		}

	}
	
	public Result deleteIndividualByName(String individualName){
		individualName = utils.nameToUri(individualName, ontologyReader.getOntologyNamespace());

		return ok(ontologyGenerator.removeIndividual(individualName)+"");
	}

}
