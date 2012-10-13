package models.owlGeneration;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.ClassCondition;
import models.ConfigurationException;
import models.PropertyValueCondition;
import models.propertyConditions.ClassValueCondition;
import models.propertyConditions.DatatypePropertyCondition;
import models.propertyConditions.IndividualValueCondition;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.NamespaceUtil;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

import de.derivo.sparqldlapi.QueryEngine;



import play.db.DB;

public class OntologyGenerator {
	private static OntologyGenerator instance;
	private static OWLDataFactory factory;
	private static OWLOntologyManager manager;
	
	private final ClassRestrictionGenerator classRestrictionGenerator;
	private IndividualGenerator individualGenerator;

	public ClassRestrictionGenerator getClassRestrictionGenerator() {
		return classRestrictionGenerator;
	}
	
	public IndividualGenerator getIndividualGenerator() {
		return individualGenerator;
	}
	
	public static void setGlobalInstance(OntologyGenerator kb) {
		instance = kb;
	}

	public static OntologyGenerator getGlobalInstance() {
		return instance;
	}
	
	public static OWLDataFactory getOwlApiFactory(){
		return factory;
	}
	

	public static void initialize(String uri, String localOntologyFolder) {
		setGlobalInstance(loadFromFile(uri, localOntologyFolder));
	}

	public static OntologyGenerator loadFromFile(String uri, String localOntologyFolder) {
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		
		return new OntologyGenerator();
	}
	
	private OntologyGenerator() {
		classRestrictionGenerator = new ClassRestrictionGenerator(factory);
		individualGenerator = new IndividualGenerator(factory);
	}


	public String convertToOwlClass(String classUri, ClassCondition condition) {
		OWLOntology destinationOntology;
		try {
			destinationOntology = manager.createOntology();

			OWLClassExpression resultExpression = getClassRestrictionGenerator().convertToOntClass(classUri, condition);
			
			addToOntologyAsClass(destinationOntology, resultExpression, classUri);
			
			return serializeToString(destinationOntology);
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}			
	}
	
	public String convertToOwlIndividual(String individualUri, ClassCondition condition) {
		OWLOntology destinationOntology;
		try {
			destinationOntology = manager.createOntology();

			List<OWLAxiom> axioms = getIndividualGenerator().convertToOntIndividual(individualUri, condition);
			
			addToOntologyAsIndividualDescription(destinationOntology, axioms);
			
			return serializeToString(destinationOntology);
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}

	private void addToOntologyAsIndividualDescription(OWLOntology destinationOntology, List<OWLAxiom> axioms) {
		for (OWLAxiom owlAxiom : axioms) {
			manager.addAxiom(destinationOntology, owlAxiom);
		}		
	}

	private void addToOntologyAsClass(OWLOntology destinationOntology, OWLClassExpression resultExpression, String conditionUri) {
		OWLClass resultClass = factory.getOWLClass(IRI.create(conditionUri));
		
		OWLAxiom equivalentClassAxiom = factory.getOWLEquivalentClassesAxiom(resultClass, resultExpression);
		
		manager.addAxiom(destinationOntology, equivalentClassAxiom);
	}

	private String serializeToString(OWLOntology destinationOntology) throws OWLOntologyStorageException {
		StringDocumentTarget serializationTarget = new StringDocumentTarget();
		
		manager.saveOntology(destinationOntology, serializationTarget);
		return serializationTarget.toString();
	}

	

	
}
