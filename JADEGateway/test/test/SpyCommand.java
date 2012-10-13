package test;

import java.util.UUID;

import agent.Command;
import agent.ConversationRegistry;
import agent.MessageSender;

public class SpyCommand implements Command {

	private boolean wasExecuted = false;
	private String conversationId;
	
	public SpyCommand(){
		conversationId = UUID.randomUUID().toString();
	}
	
	public void executeCommand(ConversationRegistry registry, MessageSender messageSender) {
		wasExecuted = true;
	}

	public String getConversationId() {
		return conversationId;
	}

	public boolean wasExecuted(){
		return wasExecuted;
	}
}
