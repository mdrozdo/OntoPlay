package messages;

import conf.Config;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import agent.ACLBaseMessage;

public class SendJobDescriptionMessage extends ACLBaseMessage {
	private String jobDescriptionOWL;

	public SendJobDescriptionMessage(String jobDescriptionOWL, String recipientAID) {
		super(recipientAID);
		this.jobDescriptionOWL = jobDescriptionOWL;
	}

	@Override
	protected ACLMessage createMessage() {
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		
		message.setContent(jobDescriptionOWL);
		message.setOntology(Config.GUIOntology);
		
		return message;
	}

}
