package test;

import jade.lang.acl.ACLMessage;

import java.util.List;

import agent.Command;
import agent.ConversationRegistry;
import agent.Gateway;
import agent.GatewayException;
import agent.commands.GetRepliesCommand;

public class TestGateway extends Gateway {
	private List<ACLMessage> replies;
	private Command executedCommand;

	public void setReplies(List<ACLMessage> replies) {
		this.replies = replies;
	}

	public List<ACLMessage> getReplies() {
		return replies;
	}

	public void setExecutedCommand(Command executedCommand) {
		this.executedCommand = executedCommand;
	}

	public Command getExecutedCommand() {
		return executedCommand;
	}
	
	@Override
	protected void executeCommand(Command command) throws GatewayException {
		executedCommand = command;
		if(executedCommand instanceof GetRepliesCommand){
			GetRepliesCommand repliesCommand = (GetRepliesCommand) command;
			repliesCommand.setReplies(replies);
		}
	}
	
}
