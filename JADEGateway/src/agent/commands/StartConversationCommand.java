package agent.commands;

import java.util.UUID;

import agent.Conversation;
import agent.ConversationRegistry;
import agent.Command;
import agent.Message;
import agent.MessageSender;

public class StartConversationCommand implements Command {

	private String conversationId;
	private final Message message;

	public StartConversationCommand(Message message){
		this.message = message;
		conversationId = UUID.randomUUID().toString();
		message.setConversationId(conversationId);
	}
	
	public void executeCommand(ConversationRegistry registry, MessageSender messageSender) {
		System.out.println(String.format("Starting conversation %s", conversationId));
		Conversation conversation = new Conversation(conversationId);
		registry.addConversation(conversation);
		messageSender.sendMessage(message);
	}

	public String getConversationId() {
		return conversationId;
	}

}
