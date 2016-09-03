package test;

import static org.junit.Assert.*;

import java.util.List;

import jade.core.AID;
import jade.core.AgentContainer;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.gateway.JadeGateway;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import agent.Gateway;
import agent.GatewayException;
import agent.Reply;

public class GatewayIntegrationTests {

	@BeforeClass
	public static void initializeGateway(){
		//jade.Boot.main(new String[]{});
		jade.wrapper.AgentContainer container = jade.core.Runtime.instance().createMainContainer(new ProfileImpl(true));
				
		Properties p = new Properties();
		
		p.setProperty(Profile.PLATFORM_ID,"192.168.131.65:1099/JADE");
        p.setProperty(Profile.MAIN_PORT, "1099");
        p.setProperty(Profile.MAIN_HOST,"192.168.131.65");
        //p.setProperty(Profile.CONTAINER_NAME, "Container-2");
        p.setProperty(Profile.LOCAL_HOST,"localhost");
        p.setProperty(Profile.LOCAL_PORT, "1100");
        p.setProperty(Profile.SERVICES,"jade.core.event.NotificationService");
        JadeGateway.init("agent.MyGatewayAgent",p );
        
	}
	
	@AfterClass
	public static void shutDownGateway(){
		JadeGateway.shutdown();
		jade.core.Runtime.instance().shutDown();
		
	}
	
	@Test
	public void startConversation_sends_message_and_returns_conversation_id_immediately() throws Exception {
		Gateway gateway = new Gateway();
		SpyMessage message = new SpyMessage();
		
		String conversationId = gateway.startConversation(message);
		
		assertTrue(!"".equals(conversationId));
		assertTrue(message.wasSent());
		assertNotNull(message.getActualSender());
	}
	
	@Test
	public void sendMessage_sends_message_and_returns_immediately() throws Exception {
		Gateway gateway = new Gateway();
		SpyMessage message = new SpyMessage();
		
		gateway.sendMessage("123", message);
		
		assertTrue(message.wasSent());
		assertNotNull(message.getActualSender());
	}
	
	@Test
	public void Requesting_responses_returns_all_messages_part_of_conversation() throws Exception{
		Gateway gateway = new Gateway();
		ACLMessage aclMessage = new ACLMessage(ACLMessage.AGREE);
		aclMessage.addReceiver(new AID("ControlContainer-1", AID.ISLOCALNAME));
		aclMessage.setContent("Test message");
		TestMessage message = new TestMessage(aclMessage);
		
		String conversationId = gateway.startConversation(message);
		Thread.sleep(50);
		
		List<Reply> replies = gateway.getResponses(conversationId);
		assertEquals(1, replies.size());
		assertEquals(aclMessage.toString(), replies.get(0).getMessage().toString());
	}	
	
}
