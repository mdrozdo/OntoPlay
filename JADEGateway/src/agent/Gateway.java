package agent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import agent.commands.GetRepliesCommand;
import agent.commands.SendMessageCommand;
import agent.commands.StartConversationCommand;
import test.SpyMessage;
import test.TestGateway;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.JadeGateway;

public class Gateway {
	private static Gateway instance;

	public String startConversation(Message message) throws GatewayException {
		StartConversationCommand command = new StartConversationCommand(message);
		executeCommand(command);
		return command.getConversationId();
	}

	protected void executeCommand(Command command) throws GatewayException {
		try {
			JadeGateway.execute(command, 200);			
		} catch (StaleProxyException e) {
			e.printStackTrace();
			throw new GatewayException(e);
		} catch (ControllerException e) {
			e.printStackTrace();
			throw new GatewayException(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new GatewayException(e);
		}
	}

	public void sendMessage(String conversationId, Message message) throws GatewayException {
		SendMessageCommand command = new SendMessageCommand(conversationId, message);
		executeCommand(command);
	}

	public List<Reply> getResponses(String conversationId) throws GatewayException {
		GetRepliesCommand command = new GetRepliesCommand(conversationId);
		executeCommand(command);
		List<ACLMessage> messages = command.getReplies();
		
		if (messages != null) {
		List<Reply> replies = new ArrayList<Reply>(messages.size());
		for (ACLMessage message : messages) {
			replies.add(new Reply(message));
		}
		return replies;
		}
		else 
			return new ArrayList<Reply>();
	}

	public static void initialize(String agentClassName, Properties jadeProfile) {
		JadeGateway.init(agentClassName, jadeProfile);
		instance = new Gateway();
	}
	
	public static Gateway getInstance(){		
		return instance;
	}

	public static void setInstance(Gateway instance) {
		Gateway.instance = instance;		
	}
}
