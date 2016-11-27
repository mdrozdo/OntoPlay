package ontoplay.controllers.webservices;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntology;

import com.google.gson.GsonBuilder;

import ontoplay.controllers.OntologyController;
import ontoplay.controllers.utils.OntologyHelper;
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

public class Classes extends OntologyController{
	public static Result getClassesByProperty(String propertyUri){
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
	
	public static Result addClassExpression() {
		@SuppressWarnings("deprecation")
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		String conditionJson = dynamicForm.get("conditionJson");
		String classExpressionName = dynamicForm.get("name");
		try {
		ClassCondition condition = ConditionDeserializer.deserializeCondition(ontologyReader, conditionJson);
		OWLOntology generatedOntology=
				ontologyGenerator.convertToOwlClassOntology(OntologyHelper.nameSpace +classExpressionName, condition);
			if (generatedOntology == null)
				return ok("Ontology is null");
			OntologyHelper.checkOntology(generatedOntology);
			OntologyReader checkOntologyReader = OntologyHelper.checkOwlReader();
			OntologyHelper.saveOntology(generatedOntology);
			return ok("ok");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("qwe");
			return ok(e.getMessage());
		}
	}
}
