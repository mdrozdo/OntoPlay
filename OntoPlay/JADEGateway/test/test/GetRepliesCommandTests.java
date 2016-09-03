package test;

import static org.junit.Assert.assertEquals;
import jade.lang.acl.ACLMessage;

import org.junit.Before;
import org.junit.Test;

import agent.Conversation;
import agent.ConversationRegistry;
import agent.Reply;
import agent.commands.GetRepliesCommand;
import agent.commands.SendMessageCommand;

public class GetRepliesCommandTests {

	@Test
	public void Executing_command_fills_replies_with_conversation_replies() throws Exception {
		String conversationId = "123";
		
		Conversation conv = new Conversation(conversationId);		
		ConversationRegistry registry = new ConversationRegistry();
		registry.addConversation(conv);
		ACLMessage reply = new ACLMessage(ACLMessage.AGREE);
		registry.addReply(conversationId, reply);
		
		GetRepliesCommand command = new GetRepliesCommand(conversationId);
		
		command.executeCommand(registry, null);
		
		assertEquals(1, command.getReplies().size());
		assertEquals(reply, command.getReplies().get(0));
	}
	
	
}
