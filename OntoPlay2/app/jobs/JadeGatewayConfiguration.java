package jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import conf.Config;

import agent.Gateway;
import agent.Reply;
import play.Play;
import test.TestGateway;
import jade.core.PlatformManager;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.JadeGateway;

//TODO: Run this on startup
public class JadeGatewayConfiguration{
	
        public void doJob() throws Exception {
		Properties p = new Properties();
  
        // -platform-id JadePlatform -local-host localhost -local-port 1099 -services jade.core.event.NotificationService

        p.setProperty(Profile.PLATFORM_ID, Config.getPlatformId());
        p.setProperty(Profile.MAIN_PORT, Config.getMainPort());
        p.setProperty(Profile.MAIN_HOST,Config.getMainHost());
        p.setProperty(Profile.CONTAINER_NAME, Config.getContainerName());
        p.setProperty(Profile.LOCAL_HOST, Config.getLocalhost());
        p.setProperty(Profile.LOCAL_PORT, Config.getLocalPort());
        p.setProperty(Profile.SERVICES, Config.getServices());
        
        Gateway.initialize("agent.MyGatewayAgent",p );
        //Test only
//        TestGateway testGateway = new TestGateway();
//        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
//        reply.setContent(FileUtils.readFileToString(new File("test/teamListReply2.xml")));
//        ArrayList<ACLMessage> replies = new ArrayList<ACLMessage>();
//        replies.add(reply);
//		testGateway.setReplies(replies);
//		Gateway.setInstance(testGateway);
//        List<ACLMessage> replies = new ArrayList<ACLMessage>();
//        // TODO How do we know that CIC needs contract info?
//        ACLMessage replyMessage1 = new ACLMessage(ACLMessage.REQUEST);
//        replyMessage1.setContent("Test reply 1");
//        ACLMessage replyMessage2 = new ACLMessage(ACLMessage.REQUEST);
//        replyMessage2.setContent("Describe contract.");
//        replies.add(replyMessage1);
//		replies.add(replyMessage2);
//       Gateway.getInstance().setReplies(replies);
        System.out.println("Initialized JadeGateway.");
        System.out.println(JadeGateway.isGatewayActive());    
        	
	}
}
