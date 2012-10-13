package test;

import jade.lang.acl.ACLMessage;
import agent.ACLMessageSender;
import agent.Message;
import agent.MessageSender;

public class TestMessage implements Message{

	private final ACLMessage[] messages;
	private String conversationId;

	public TestMessage(ACLMessage... messages){
		this.messages = messages;
		
	}
	
	public void send(ACLMessageSender sender) {
		for (ACLMessage message : messages) {
			message.setConversationId(conversationId);
			sender.send(message);	
		}
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId; 
	}

}
