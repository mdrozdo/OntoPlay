package ontoplay.controllers.webservices;

import java.util.ArrayList;
import java.util.List;

import ontoplay.OntologyHelper;
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
import play.mvc.Result;

import javax.inject.Inject;

public class Classes extends OntologyController{

	private OntologyReader ontologyReader;
	private OntologyGenerator ontologyGenerator;
	private OntologyUtils utils;

	@Inject
	public Classes(OntologyHelper ontologyHelper, OntologyReader ontologyReader, OntologyGenerator ontologyGenerator, OntologyUtils utils){
		super(ontologyHelper);
		this.ontologyReader = ontologyReader;
		this.ontologyGenerator = ontologyGenerator;
		this.utils = utils;
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
	
	public Result addClassExpression() {
		@SuppressWarnings("deprecation")
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		String conditionJson = dynamicForm.get("conditionJson");
		String classExpressionName = dynamicForm.get("name");
		try {
		ClassCondition condition = ConditionDeserializer.deserializeCondition(ontologyReader, conditionJson);
		OWLOntology generatedOntology=
				ontologyGenerator.convertToOwlClassOntology(utils.joinNamespaceAndName(ontologyReader.getOntologyNamespace(), classExpressionName), condition);
			if (generatedOntology == null)
				return ok("Ontology is null");
			ontoHelper.checkOntology(generatedOntology);
			OntologyReader checkOntologyReader = ontoHelper.checkOwlReader();
			utils.saveOntology(generatedOntology);
			return ok("ok");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("qwe");
			return ok(e.getMessage());
		}
	}
}
