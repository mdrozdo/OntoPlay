package test;

import java.util.UUID;

import jade.core.Agent;
import agent.ACLMessageSender;
import agent.ConversationRegistry;
import agent.Command;
import agent.Message;
import agent.MessageSender;

public class SpyMessage implements Message {

	private boolean wasExecuted = false;
	private String conversationId;
	private ACLMessageSender actualSender;
	
	public SpyMessage(){
	}
	
	public boolean wasSent() {
		return wasExecuted ;
	}

	public void send(ACLMessageSender sender) {
		wasExecuted = true;	
		actualSender = sender;
	}

	public String getConversationId() {
		return conversationId;
	}

	public ACLMessageSender getActualSender() {
		return actualSender;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
		
	}

}
