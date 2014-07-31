package messages;

import conf.Config;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import agent.ACLBaseMessage;

public class FindTeamsForExecutingJobMessage extends ACLBaseMessage {
	private String teamConstraintsOwl;

	public FindTeamsForExecutingJobMessage(String teamConstraintsOwl, String recipientAID) {
		super(recipientAID);
		this.teamConstraintsOwl = teamConstraintsOwl;
	}

	@Override
	protected ACLMessage createMessage() {
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		//TODO: Use Pawel's library in some way to send the OWL description of the resource looking for teams to join.
		//TODO: Do we need new message classes in the ontology for these messages? 
		message.setContent(teamConstraintsOwl);
		message.setOntology(Config.GUIOntology);
		return message;
	}

}
