package controllers.samples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.Secure.Security;

import jade.wrapper.gateway.JadeGateway;
import jobs.JenaOwlReaderConfiguration;
import agent.Gateway;
import agent.GatewayException;
import agent.Reply;
import messages.FindTeamsLookingForMemberMessage;
import messages.SendJobDescriptionMessage;
import messages.SendWorkerContractConstraintsMessage;
import models.ClassCondition;
import models.ConditionDeserializer;
import models.ontologyModel.OntoClass;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;
import models.owlGeneration.OntologyGenerator;
import play.mvc.*;

//TODO: This should generate individual describing the configuration, instead of constraints
public class PizzaSample extends Controller {

	private static OntologyReader ontologyReader;
	private static OntologyGenerator owlApi;
	private final static String PIZZA_NS = "http://www.co-ode.org/ontologies/pizza/pizza.owl#";
	// TODO: Eventually this should be kept in a database
	private static Map<String, ClassCondition> conditions = new HashMap<String, ClassCondition>();

	public static void index() {
		ontologyReader = createOwlReader();
		owlApi = createOwlGenerator();
		OntoClass owlClass = ontologyReader.getOwlClass(PIZZA_NS + "Pizza");

		render(owlClass);
	}

	private static OntologyGenerator createOwlGenerator() {
		return OntologyGenerator.loadFromFile("file:test/pizza.owl", "./test");
	}

	private static OntologyReader createOwlReader() {
		new JenaOwlReaderConfiguration().initialize("file:test/pizza.owl", new JenaOwlReaderConfig()
			.useLocalMapping("http://www.co-ode.org/ontologies/pizza/pizza.owl", "file:test/pizza.owl"));
		return OntologyReader.getGlobalInstance();
	}

	public static void updateConditions(String conditionJson) {
		ClassCondition condition = ConditionDeserializer.deserializeCondition(ontologyReader, conditionJson);
		String conditionRdf = owlApi.convertToOwlIndividual(PIZZA_NS + "NewPizza", condition);
		updateSuccessful(conditionRdf);
	}

	// public static void sendContractConstraints(String conversationId, String
	// conditionJson){
	// ClassCondition condition =
	// ConditionDeserializer.deserializeCondition(jena, conditionJson);
	// String conditionRdf =
	// owlApi.convertToOwlClass("http://gridagents.sourceforge.net/TeamConditions#ConstraintsCondition",
	// condition);
	//    	
	// Gateway gateway = Gateway.getInstance();
	// try {
	// gateway.sendMessage(conversationId, new
	// SendWorkerContractConstraintsMessage(conditionRdf,
	// Security.connected()));
	// conditions.put(conversationId, condition);
	// monitorSearchForTeams(conversationId);
	// } catch (GatewayException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static void updateSuccessful(String response) {
		render(response);
	}
}