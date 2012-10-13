package messages;

import conf.Config;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import agent.ACLBaseMessage;

public class FindTeamsLookingForMemberMessage extends ACLBaseMessage {
	private String resourceDescriptionOWL;

	public FindTeamsLookingForMemberMessage(String resourceDescriptionOWL, String recipientAID) {
		super(recipientAID);
		this.resourceDescriptionOWL = resourceDescriptionOWL;
	}

	@Override
	protected ACLMessage createMessage() {
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		//TODO: Use Pawel's library in some way to send the OWL description of the resource looking for teams to join.
		message.setContent(resourceDescriptionOWL);
		message.setOntology(Config.GUIOntology);
		
		return message;
	}

}
