package agent;

public interface Message {
	void send(ACLMessageSender sender);

	void setConversationId(String conversationId);
}
