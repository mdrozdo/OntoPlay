package ontoplay.controllers.webservices;

import java.util.ArrayList;
import java.util.List;

import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.owlGeneration.OntologyGenerator;
import org.semanticweb.owlapi.model.OWLOntology;

import com.google.gson.GsonBuilder;

import ontoplay.controllers.OntologyController;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.ClassCondition;
import ontoplay.models.ConditionDeserializer;
import ontoplay.models.ConfigurationException;
import ontoplay.models.angular.ClassDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.OntologyReader;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;

public class Classes extends OntologyController{

	private OntologyReader ontologyReader;
	private OntologyGenerator ontologyGenerator;
	private OntologyUtils utils;
	private FormFactory formFactory;

	@Inject
	public Classes(OntologyUtils ontologyUtils, OntologyReader ontologyReader, OntologyGenerator ontologyGenerator, OntologyUtils utils, FormFactory formFactory){
		super(ontologyUtils);
		this.ontologyReader = ontologyReader;
		this.ontologyGenerator = ontologyGenerator;
		this.utils = utils;
		this.formFactory = formFactory;
	}

	public Result getClassesByProperty(String propertyUri){
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

	public Result getClasses(){
		List<OntoClass> classes=ontologyReader.getClasses();
		List<ClassDTO> classesDTO=new ArrayList<ClassDTO>();
		for (OntoClass owlClass : classes) {
            classesDTO.add(new ClassDTO(owlClass));
        }

		return ok(new GsonBuilder().create().toJson(classesDTO));
	}
	
	public Result addClassExpression() {
		DynamicForm dynamicForm = formFactory.form().bindFromRequest();
		String conditionJson = dynamicForm.get("conditionJson");
		String classExpressionName = dynamicForm.get("name");
		try {
		ClassCondition condition = ConditionDeserializer.deserializeCondition(ontologyReader, conditionJson);
		OWLOntology generatedOntology=
				ontologyGenerator.convertToOwlClassOntology(utils.joinNamespaceAndName(ontologyReader.getOntologyNamespace(), classExpressionName), condition);
			if (generatedOntology == null)
				return ok("Ontology is null");
			ontologyUtils.checkOntology(generatedOntology);
			//OntologyReader checkOntologyReader = ontologyUtils.checkOwlReader();
			utils.saveOntology(generatedOntology);
			return ok("ok");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("qwe");
			return ok(e.getMessage());
		}
	}
}
