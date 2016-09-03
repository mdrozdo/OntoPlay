package agent;

/*****************************************************************

 This agent receives the blackboard object  
 and its content will be sent to the proper agent

 *****************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.gateway.GatewayAgent;

public class MyGatewayAgent extends GatewayAgent 
	implements MessageSender, ACLMessageSender {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8445440945745613865L;

	public MyGatewayAgent(){
		super();
		System.out.println("Gateway agent created");
	}
	
	private ConversationRegistry conversationRegistry = new ConversationRegistry();
		
	@Override
	protected void processCommand(java.lang.Object obj) {
		if (obj instanceof Command) {
			Command command = (Command) obj;
			
			try{
				command.executeCommand(conversationRegistry, this);				
			}
			finally{
				releaseCommand(command);
			}
		}
	}

	@Override
	public void setup() {
		System.out.println(String.format("Gateway agent setting up (AID:%s).", getAID()));
		// Waiting for the answer
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {

				ACLMessage msg = receive();

				if ((msg != null)) {
					conversationRegistry.addReply(msg.getConversationId(), msg);

				} else
					block();
			}
		});

		super.setup();
	}	
	
	public void sendMessage(Message message) {
		message.send(this);
	}

}