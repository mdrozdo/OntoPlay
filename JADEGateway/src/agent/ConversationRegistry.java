package agent;

import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;


public class ConversationRegistry {

	private Map<String, Conversation> conversations = new HashMap<String, Conversation>();

	public void processCommand(Command command) {
		command.executeCommand(this, null);
	}

	public void addConversation(Conversation conversation) {
		conversations.put(conversation.getConversationId(), conversation);
	}

	public Conversation getConversation(String conversationId) {
		return conversations.get(conversationId);
	}

	public List<ACLMessage> getReplies(String conversationId) {
		Conversation conv = conversations.get(conversationId);
		if(conv != null)
			return conv.getReplies();
		else{
			System.out.printf("Received a reply from an unknown conversation (id:%s). Returning empty list.", conversationId);
			return new ArrayList<ACLMessage>(0);
		}
	}


	public void addReply(String conversationId, ACLMessage reply) {
		Conversation conv = conversations.get(conversationId);
		if(conv != null){
			conv.addReply(reply);
		}
		else{
			System.out.printf("Received a reply from an unknown conversation (id:%s). Ignoring the reply.", conversationId);
		}
		
	}

}
