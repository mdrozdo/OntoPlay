package ontoplay.models;

import jade.lang.acl.ACLMessage;
import jadeOWL.base.OntologyManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;

import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import agent.Reply;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JadeOwlMessageReader {
	private static JadeOwlMessageReader instance;
	
	public static void setGlobalInstance(JadeOwlMessageReader jadeOwl) {
		instance = jadeOwl;
	}

	public static JadeOwlMessageReader getGlobalInstance() {
		return instance;
	}

	
	private OntologyManager ontologyManager;
	public JadeOwlMessageReader(JenaOwlReaderConfig config) {
		this.ontologyManager = new OntologyManager();
		if (config != null) {
			
			for (Map.Entry<String, String> mapping : config.getLocalMappings().entrySet()) {
				ontologyManager.addIRIMapping(mapping.getKey(), mapping.getValue());
			}
		}
	}
	
	public List<OwlIndividual> getIndividualsFromAnswerSet(Reply reply){
    	OWLOntology ontology;
    	ACLMessage message = reply.getMessage();
		try {
			ontology = ontologyManager.getOntologyFromACLMessage(message);
			
			//Get individuals from the query answer
			Set<OWLNamedIndividual> individuals = ontologyManager.getQueryManager().filterAnswerSetInstances(ontology);
			
			List<OwlIndividual> result = new ArrayList<OwlIndividual>();
			for (Iterator iterator = individuals.iterator(); iterator.hasNext();) {
				OWLNamedIndividual owlApiIndividual = (OWLNamedIndividual) iterator.next();
				OwlIndividual individual = new OwlIndividual(owlApiIndividual, new ArrayList<PropertyValueCondition>());
				result.add(individual);
			}
			return result;
		}catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ACLMessage fillMessageWithFilteredAnswerSet(Reply reply, ACLMessage newMessage, List<String> filteredUris){
    	OWLOntology ontology;
    	ACLMessage message = reply.getMessage();
		try {
			ontology = ontologyManager.getOntologyFromACLMessage(message);
			
			//Get individuals from the query answer
			Set<OWLNamedIndividual> individuals = ontologyManager.getQueryManager().filterAnswerSetInstances(ontology);
			ontologyManager.getQueryManager().removeAnswerSetAxioms(ontology);
			
			Set<OWLNamedIndividual> filteredIndividuals = filterByUris(individuals, filteredUris);
			
			OWLOntology resultOnto = ontologyManager.getQueryManager().prepareQueryAnswerFromInstances(filteredIndividuals, ontology);
			
			ontologyManager.fillJadeACLMessageContent(newMessage, resultOnto);
			return newMessage;
			
		}catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return null;
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Set<String> readJobResultFileUrls(ACLMessage jobResultMessage) throws OWLOntologyCreationException{
		OWLOntology jobResult = ontologyManager.getOntologyFromACLMessage(jobResultMessage);
		
		Set<String> urls = new HashSet<String>();
		
		PelletReasoner reasoner = PelletReasonerFactory.getInstance().createNonBufferingReasoner(jobResult);
		KnowledgeBase kb = reasoner.getKB();
		PelletInfGraph graph = new org.mindswap.pellet.jena.PelletReasoner().bind(kb);
		// Wrap the graph in a model
		InfModel model = ModelFactory.createInfModel(graph);
		QueryExecution qe = null;
		ResultSet rs;

		try {
		    String qry = 
		    	"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"+
		    	"PREFIX cond:<http://gridagents.sourceforge.net/AiGConditionsOntology#> \r\n"+
		    	"PREFIX cgo:<http://www.owl-ontologies.com/unnamed.owl#>\r\n"+
		    	" SELECT ?url WHERE { \n" +
		    	"?result rdf:type cond:JobResult .\n"+
		    	"?result cond:containsResource ?resource .\n"+
		    	"?resource rdf:type cond:JobResource .\n"+
		    	"?resource cgo:hasID ?x .\n"+
		    	"?x rdf:type cgo:URL .\n"+
		    	"?x cgo:hasName ?url }"; 
		    qe = QueryExecutionFactory.create(qry, model); 
		    rs = qe.execSelect(); 

		    if(!rs.hasNext()){
		    	System.out.println("Found none.");
		    }
		    while(rs.hasNext()) {
		        QuerySolution sol = rs.nextSolution(); 
		        String url = sol.get("url").asLiteral().getString();
				System.out.println(url);
				urls.add(url);
		    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		finally {
		    qe.close();
		    model.close();
		    graph.close();
		}
		
		return urls;
	}

	private Set<OWLNamedIndividual> findIndividualsOfClass(OWLOntology ontology, IRI classURI) {
		Set<OWLNamedIndividual> individuals = null;

		OWLDataFactory dataFactory = ontologyManager.getOWLDataFactory();
		OWLClass owlClass = dataFactory.getOWLClass(classURI);

		try {
			individuals = ontologyManager.getQueryManager().getInstancesForClassQuery(owlClass, ontology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return individuals;
	}

	private static Set<OWLNamedIndividual> filterByUris(Set<OWLNamedIndividual> individuals, List<String> filteredUris) {
		Set<OWLNamedIndividual> filteredTeams = new HashSet<OWLNamedIndividual>();
		for (OWLNamedIndividual owlIndividual : individuals) {
			for (String teamUri : filteredUris) {
				if(teamUri.equals(owlIndividual.getIRI().toString())){
					filteredTeams.add(owlIndividual);
				}
			}
		}
		return filteredTeams;
	}
	public static void initialize(JenaOwlReaderConfig mappingConfig) {
		setGlobalInstance(new JadeOwlMessageReader(mappingConfig));		
	}	
}
