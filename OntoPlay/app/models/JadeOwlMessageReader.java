package models;

import jade.lang.acl.ACLMessage;
import jadeOWL.base.OntologyManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.ontologyModel.OwlIndividual;
import models.ontologyReading.jena.JenaOwlReaderConfig;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;

import agent.Reply;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
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
