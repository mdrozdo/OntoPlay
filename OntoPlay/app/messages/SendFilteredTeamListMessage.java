package messages;

import java.util.List;

import models.JadeOwlMessageReader;
import models.ontologyModel.OwlIndividual;

import conf.Config;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import agent.ACLBaseMessage;
import agent.Reply;

public class SendFilteredTeamListMessage extends ACLBaseMessage {
	private List<String> teamUris;
	private JadeOwlMessageReader jadeOwl;
	private Reply previousReply;

	public SendFilteredTeamListMessage(Reply previousReply, List<String> filteredTeamUris, String recipientAID) {
		super(recipientAID);
		this.previousReply = previousReply;
		teamUris = filteredTeamUris;
		jadeOwl = JadeOwlMessageReader.getGlobalInstance();
	}

	@Override
	protected ACLMessage createMessage() {
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		jadeOwl.fillMessageWithFilteredAnswerSet(previousReply, message, teamUris);
		message.setOntology(Config.GUIOntology);
		
		return message;
	}

}
