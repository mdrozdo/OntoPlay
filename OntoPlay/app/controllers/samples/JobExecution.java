package controllers.samples;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import controllers.MySecure;
import controllers.OntologyController;
import controllers.Secure;
import controllers.Secure.Security;

import jade.wrapper.gateway.JadeGateway;
import jobs.JenaOwlReaderConfiguration;
import agent.Gateway;
import agent.GatewayException;
import agent.Reply;
import messages.FindTeamsForExecutingJobMessage;
import messages.FindTeamsLookingForMemberMessage;
import messages.SendFilteredTeamListMessage;
import messages.SendJobDescriptionMessage;
import messages.SendJobExecutionConditionsMessage;
import messages.SendWorkerContractConstraintsMessage;
import models.ClassCondition;
import models.ConditionDeserializer;
import models.JadeOwlMessageReader;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OwlIndividual;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;
import models.owlGeneration.OntologyGenerator;
import play.mvc.*;

@With(MySecure.class)
public class JobExecution extends OntologyController {

	private static JadeOwlMessageReader jadeOwl = JadeOwlMessageReader.getGlobalInstance();
	private static OntologyGenerator owlApi = OntologyGenerator.getGlobalInstance();
	private final static String AIGCO_NS = "http://gridagents.sourceforge.net/AiGConditionsOntology#";

	private static Map<String, Reply> latestReplies = new HashMap<String, Reply>();

	private final static String CGO_NS = "http://www.owl-ontologies.com/unnamed.owl#";

	public static void index() {
		try {
			String uri = "file:test/AiGConditionsOntology.owl";
			JenaOwlReaderConfig jenaOwlReaderConfig = new JenaOwlReaderConfig()
				.useLocalMapping("http://gridagents.sourceforge.net/AiGGridOntology", "file:test/AiGGridOntology.owl")
				.useLocalMapping("http://www.w3.org/2006/time", "file:test/time.owl")
				.useLocalMapping("http://www.owl-ontologies.com/unnamed.owl", "file:test/cgo.owl")
				.ignorePropertiesWithUnspecifiedDomain();
			
			new JenaOwlReaderConfiguration().initialize(uri, jenaOwlReaderConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		OntoClass owlClass = getOntologyReader().getOwlClass(CGO_NS + "ComputingElement");

		render(owlClass);
	}

	public static void updateConditions(String conditionJson) {
		ClassCondition condition = ConditionDeserializer.deserializeCondition(getOntologyReader(), conditionJson);
		String conditionRdf = owlApi.convertToOwlClass("http://gridagents.sourceforge.net/TeamConditions#TeamCondition", condition);

		Gateway gateway = Gateway.getInstance();
		try {
			String conversationId = gateway.startConversation(new FindTeamsForExecutingJobMessage(conditionRdf, Security.connected()));
			monitorConversation(conversationId);
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendTeamList(String conversationId, List<String> teamUris) {
		Gateway gateway = Gateway.getInstance();
		try {
			Reply baseTeamListReply = latestReplies.get(conversationId);
			gateway.sendMessage(conversationId, new SendFilteredTeamListMessage(baseTeamListReply, teamUris, Security.connected()));
			monitorConversation(conversationId);
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void contractRequirementsTest(){
		OntoClass owlClass = getOntologyReader().getOwlClass(AIGCO_NS + "JobExecutionConditions");
		String conversationId = "123";
		render(conversationId, owlClass);
	}

	public static void getReplies(String conversationId) {
		List<Reply> replies;
		try {
			replies = Gateway.getInstance().getResponses(conversationId);
			// TODO Add some more generic way of registering reply handlers to
			// make it easier to handle message protocols.
			if (contractParametersNeeded(replies)) {
				OntoClass owlClass = getOntologyReader().getOwlClass(AIGCO_NS + "JobExecutionConditions");
				renderTemplate("JobExecution/contractRequirements.html", conversationId, owlClass);
			} else if (receivedListOfTeams(replies)) {
				Reply listOfTeamsMessage = getListOfTeamsMessage(replies);
				List<OwlIndividual> teams = jadeOwl.getIndividualsFromAnswerSet(listOfTeamsMessage);
				latestReplies.put(conversationId, listOfTeamsMessage);
				renderTemplate("JobExecution/listTeams.html", conversationId, teams);
			} 
			else if(jobDescriptionNeeded(replies)){
				OntoClass owlClass = getOntologyReader().getOwlClass(AIGCO_NS + "JobDescription");
				renderTemplate("JobExecution/jobDescription.html", conversationId, owlClass);
			}
			else {
				render("Common/getReplies.html", replies); // TODO: This should
															// really display
															// different
															// messages or views
															// basing on reply
															// content
			}
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Reply getListOfTeamsMessage(List<Reply> replies) {
		return replies.get(replies.size()-1);
	}

	private static boolean receivedListOfTeams(List<Reply> replies) {
		// TODO For now, we check if there are any individuals in answer set.
		// This will not work if we want
		// to work with empty answer sets (no teams found). We need a way to
		// distinguish between types of messages.
		if (replies != null && !replies.isEmpty()) {
			Reply lastReply = replies.get(replies.size() - 1);
			if (jadeOwl.getIndividualsFromAnswerSet(lastReply) != null)
				return jadeOwl.getIndividualsFromAnswerSet(lastReply).size() > 0;
		}
		return false;
	}

	private static boolean contractParametersNeeded(List<Reply> replies) {
		if (replies.size() > 0) {
			Reply lastReply = replies.get(replies.size() - 1);
			if (lastReply.getMessage().getContent() != null && lastReply.getMessage().getContent().contains("Describe contract")) {
				return true;
			}
		}
		return false;
	}

	private static boolean jobDescriptionNeeded(List<Reply> replies) {
		if(replies.size() > 0){
			Reply lastReply = replies.get(replies.size()-1);
			//TODO Modify to match real message

			if (lastReply.getMessage().getContent() != null && lastReply.getMessage().getContent().contains("Describe job")) {
				return true;
			}
		}
		return false;
	}
	
	public static void sendExecutionConditions(String conversationId, String conditionJson) {
		ClassCondition condition = ConditionDeserializer.deserializeCondition(getOntologyReader(), conditionJson);
		String conditionRdf = owlApi.convertToOwlClass("http://gridagents.sourceforge.net/JobExecutionConditions#JobExecutionConditions", condition);
		System.out.println(conditionRdf);

		Gateway gateway = Gateway.getInstance();
		try {
			gateway.sendMessage(conversationId, new SendJobExecutionConditionsMessage(conditionRdf, Security.connected()));
			monitorConversation(conversationId);
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendJobDescription(String conversationId, String conditionJson){
    	ClassCondition condition = ConditionDeserializer.deserializeCondition(getOntologyReader(), conditionJson);
    	String conditionRdf = owlApi.convertToOwlIndividual("http://gridagents.sourceforge.net/TeamConditions#JobDescription", condition);
    	
    	Gateway gateway = Gateway.getInstance();
    	try {
			gateway.sendMessage(conversationId, new SendJobDescriptionMessage(conditionRdf, Security.connected()));
			monitorConversation(conversationId);
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    }	

	public static void monitorConversation(String conversationId) {
		render(conversationId);
	}

	public static void updateSuccessful(String condition) {
		render(condition);
	}

}
