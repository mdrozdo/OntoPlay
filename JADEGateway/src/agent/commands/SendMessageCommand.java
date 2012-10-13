package agent.commands;

import agent.Command;
import agent.ConversationRegistry;
import agent.Message;
import agent.MessageSender;

public class SendMessageCommand implements Command {

	private final String conversationId;
	private final Message message;

	public SendMessageCommand(String conversationId, Message message) {
		this.conversationId = conversationId;
		message.setConversationId(conversationId);
		this.message = message;
	}

	public void executeCommand(ConversationRegistry registry, MessageSender messageSender) {
		messageSender.sendMessage(message);
	}

	public String getConversationId() {
		return conversationId;
	}

}
