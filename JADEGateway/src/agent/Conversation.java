package agent;

import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

	private final String conversationId;
	private List<ACLMessage> replies =  new ArrayList<ACLMessage>();

	public Conversation(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public List<ACLMessage> getReplies() {
		return new ArrayList<ACLMessage>(replies);
	}

	public void addReply(ACLMessage reply) {
		replies.add(reply);
	}

}
