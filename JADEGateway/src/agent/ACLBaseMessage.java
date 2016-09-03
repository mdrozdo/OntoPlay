package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public abstract class ACLBaseMessage implements Message {

	private String conversationId;
	private final String recipientAID;

	public ACLBaseMessage(String recipientAID) {
		this.recipientAID = recipientAID;
	}
	
	public void send(ACLMessageSender sender) {
		ACLMessage message = createMessage();
		message.setConversationId(conversationId);
		message.addReceiver(new AID(recipientAID, false));
		sender.send(message);
	}

	protected abstract ACLMessage createMessage();

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;

	}

}
