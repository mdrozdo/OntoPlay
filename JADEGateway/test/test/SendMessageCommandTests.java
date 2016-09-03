package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import agent.Conversation;
import agent.ConversationRegistry;
import agent.commands.SendMessageCommand;
import agent.commands.StartConversationCommand;

public class SendMessageCommandTests {
	private SendMessageCommand command;
	private SpyMessageSender messageSender;
	private SpyMessage message;
	private ConversationRegistry registry;

	@Before
	public void initializeTest(){
		message = new SpyMessage();
		command = new SendMessageCommand("123", message);
		messageSender = new SpyMessageSender();
		registry = new ConversationRegistry();		
	}
	
	@Test
	public void Executing_command_sends_a_message() throws Exception {
		command.executeCommand(registry, messageSender);
		
		assertEquals(message, messageSender.getSentMessage());		
	}
	
	@Test
	public void Executing_command_sets_message_conversation_id(){
		command.executeCommand(registry, messageSender);
		
		assertEquals("123", message.getConversationId());
	}
}
