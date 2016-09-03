package test;

import jade.lang.acl.ACLMessage;
import agent.ACLMessageSender;
import agent.Message;
import agent.MessageSender;

public class SpyMessageSender implements MessageSender, ACLMessageSender {

	private Message sentMessage;

	public Message getSentMessage() {
		return sentMessage;
	}

	public void sendMessage(Message message) {
		sentMessage = message;		
		message.send(this);
	}

	public void send(ACLMessage message) {
		// TODO Auto-generated method stub
		
	}

}
