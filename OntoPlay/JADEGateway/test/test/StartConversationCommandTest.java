package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import agent.Conversation;
import agent.ConversationRegistry;
import agent.commands.StartConversationCommand;

public class StartConversationCommandTest {
	private StartConversationCommand command;
	private SpyMessageSender messageSender;
	private SpyMessage message;
	private ConversationRegistry registry;

	@Before
	public void initializeTest(){
		message = new SpyMessage();
		command = new StartConversationCommand(message);
		messageSender = new SpyMessageSender();
		registry = new ConversationRegistry();		
	}
	
	@Test
	public void When_created_command_has_a_unique_conversation_id() throws Exception {
		String conversationId1 = command.getConversationId();
		
		String conversationId2 = new StartConversationCommand(new SpyMessage()).getConversationId();
		
		assertNotNull(conversationId1);
		assertFalse(conversationId1.equalsIgnoreCase(conversationId2));
	}
	
	@Test
	public void Executing_command_adds_a_new_conversation_to_registry() throws Exception {
		String conversationId = command.getConversationId();
		
		command.executeCommand(registry, messageSender);
		
		Conversation conversation = registry.getConversation(conversationId);
		
		assertNotNull(conversation);		
	}
	
	@Test
	public void Executing_command_sends_a_message() throws Exception {
		command.executeCommand(registry, messageSender);
		
		assertEquals(message, messageSender.getSentMessage());		
	}
	
}
