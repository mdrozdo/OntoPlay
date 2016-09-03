package test;

import static org.junit.Assert.*;

import jade.lang.acl.ACLMessage;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import agent.Conversation;
import agent.ConversationRegistry;
import agent.Reply;

public class ConversationRegistryTests {
	private ConversationRegistry registry;

	@Before
	public void initializeTest() {
		registry = new ConversationRegistry();
	}
		
	@Test
	public void ProcessCommand_executes_command() throws Exception {
		SpyCommand command = new SpyCommand();
		
		registry.processCommand(command);
		
		assertTrue(command.wasExecuted());
	}

	@Test
	public void SetConversation_stores_conversation_by_conversation_id(){
		Conversation conversation = new Conversation("conversationId"); 
		
		registry.addConversation(conversation);
		
		assertEquals(conversation, registry.getConversation("conversationId"));		
	}
	
	@Test
	public void AddReply_stores_reply_using_conversation_id(){
		String conversationId = "conversationId";
		Conversation conversation = new Conversation(conversationId); 
		
		registry.addConversation(conversation);
		ACLMessage reply = new ACLMessage(ACLMessage.AGREE);
		registry.addReply(conversationId, reply);
		
		List<ACLMessage> replies = registry.getReplies(conversationId);
				
		assertEquals(1, replies.size());
		assertEquals(reply, replies.get(0));		
	}
	
	@Test
	public void AddReply_for_unknown_conversation_id_doesnt_throw(){
		String conversationId = "conversationId";
		
		ACLMessage reply = new ACLMessage(ACLMessage.AGREE);
		registry.addReply(conversationId, reply);		
	}
	
	@Test
	public void GetReplies_for_unknown_conversation_id_returns_empty_list(){
		String conversationId = "conversationId";
		
		 List<ACLMessage> replies = registry.getReplies(conversationId);		
		 assertEquals(0, replies.size());
	}
	
	
}
