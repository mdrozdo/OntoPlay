package agent;

import jade.lang.acl.ACLMessage;

public class Reply {

	private ACLMessage message;

	public ACLMessage getMessage() {
		return message;
	}

	public Reply(ACLMessage message) {
		this.message = message;
	}

}
