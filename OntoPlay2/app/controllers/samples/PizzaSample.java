package controllers.samples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jobs.JenaOwlReaderConfiguration;
import models.ClassCondition;
import models.ConditionDeserializer;
import models.ontologyModel.OntoClass;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;
import models.owlGeneration.OntologyGenerator;

import play.*;
import play.mvc.*;
import play.data.Form;
import play.Logger.*;

import views.html.samples.*;
import views.html.*;

//TODO: This should generate individual describing the configuration, instead of constraints
public class PizzaSample extends Controller {

	private static OntologyReader ontologyReader;
	private static OntologyGenerator owlApi;
	private final static String PIZZA_NS = "http://www.co-ode.org/ontologies/pizza/pizza.owl#";
	// TODO: Eventually this should be kept in a database
	private static Map<String, ClassCondition> conditions = new HashMap<String, ClassCondition>();

	public static Result index() {
		ontologyReader = createOwlReader();
		OntoClass owlClass = ontologyReader.getOwlClass(PIZZA_NS + "Pizza");

		return ok(pizzasample.render(owlClass));
	}

	private static OntologyGenerator createOwlGenerator() {
		return OntologyGenerator.loadFromFile("file:test/pizza.owl", "./test");
	}

	private static OntologyReader createOwlReader() {
		new JenaOwlReaderConfiguration().initialize("file:test/pizza.owl", new JenaOwlReaderConfig()
			.useLocalMapping("http://www.co-ode.org/ontologies/pizza/pizza.owl", "file:test/pizza.owl"));
		return OntologyReader.getGlobalInstance();
	}

	public static Result updateConditions() {
		owlApi = createOwlGenerator();
		String conditionJson = Form.form().bindFromRequest().get("conditionJson");
		ClassCondition condition = ConditionDeserializer.deserializeCondition(ontologyReader, conditionJson);
		ALogger log = play.Logger.of("application");
		log.info("owlapi:"+ (owlApi == null ? "yup" : "nope"));
		String conditionRdf = owlApi.convertToOwlIndividual(PIZZA_NS + "NewPizza", condition);
		return ok(conditionJson);//updateSuccessful(conditionRdf);
	}

	public static void updateSuccessful(String response) {
		//TODO: render(response);
	}
}