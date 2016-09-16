import jade.lang.acl.ACLMessage;
import jadeOWL.base.messaging.ACLOWLMessage;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jobs.PropertyTypeConfiguration;
import jobs.PropertyTypeConfiguration;
import junit.framework.Assert;

import ontoplay.models.ClassCondition;
import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.JadeOwlMessageReader;
import ontoplay.models.PropertyValueCondition;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;
import ontoplay.models.properties.OwlObjectProperty;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import agent.Reply;

import static org.fest.assertions.Assertions.assertThat;

public class JadeOwlMessageReaderTest {
	private JadeOwlMessageReader jadeOwl;

	@BeforeClass
	public static void classSetup(){		
	}
	
	@Before
	public void setup() {
		jadeOwl = new JadeOwlMessageReader(new JenaOwlReaderConfig()
			.useLocalMapping("http://gridagents.sourceforge.net/AiGGridOntology", "file:test/AiGGridOntology.owl")
			.useLocalMapping("http://www.w3.org/2006/time", "file:test/time.owl")
			.useLocalMapping("http://www.owl-ontologies.com/unnamed.owl", "file:test/cgo.owl"));	
	}

	@Test
	public void jadeOwl_retrieves_individuals_from_answer_set() throws Exception {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(FileUtils.readFileToString(new File("test/teamListReply.xml")));
		
		Reply reply = new Reply(message);
		
		List<OwlIndividual> individuals = jadeOwl.getIndividualsFromAnswerSet(reply);
		
		assertThat(selectLocalNames(individuals))
				.containsExactly("compositeWorker");
	}	
	
	@Test
	public void jadeOwl_creates_message_with_filtered_answer_set() throws Exception {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(FileUtils.readFileToString(new File("test/teamListReply2.xml")));
		
		Reply reply = new Reply(message);
		ACLMessage newMessage = new ACLMessage(ACLMessage.INFORM);
		ArrayList<String> filteredUris = new ArrayList<String>();
		filteredUris.add("http://gridagents.sourceforge.net/AiGGridOntology#compositeWorker2");
		jadeOwl.fillMessageWithFilteredAnswerSet(reply, newMessage, filteredUris);
		
		Reply newReply = new Reply(newMessage);
		List<OwlIndividual> individuals = jadeOwl.getIndividualsFromAnswerSet(newReply);
		
		assertThat(selectLocalNames(individuals))
				.containsExactly("compositeWorker2");
	}	

	@Test
	public void jadeOwl_Reads_File_URLs_From_Job_Result() throws Exception {
		ACLOWLMessage jobResultMessage = createJobResultMessage();
		Set<String> resultFileUrls = jadeOwl.readJobResultFileUrls(jobResultMessage);
		
		assertThat(resultFileUrls).containsOnly("http://tempuri.org/job1234_out.zip");
	}		
	
	
	private ACLOWLMessage createJobResultMessage() throws IOException {
		ACLOWLMessage message = new ACLOWLMessage(ACLMessage.INFORM);
		
		message.setConversationId("conversationId");
		message.setOntology("JobResult");
		message.setContent(FileUtils.readFileToString(new File("test/jobResult.owl")));
		return message;
	}

	private List<String> selectLocalNames(List<? extends OwlElement> properties) {
		List<String> names = new ArrayList<String>(properties.size());

		for (OwlElement prop : properties) {
			names.add(prop.getLocalName());
		}

		return names;
	}	
}
