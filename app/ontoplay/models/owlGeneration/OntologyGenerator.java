package ontoplay.models.owlGeneration;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import ontoplay.models.ClassCondition;
import ontoplay.models.ConfigurationException;
import ontoplay.models.OntologyUtils;

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

	public static OWLDataFactory getOwlApiFactory() {
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

	public OWLOntology convertToOwlClassOntology(String classUri, ClassCondition condition) {
		OWLOntology destinationOntology;
		try {
			IRI classIri = IRI.create(classUri);
			IRI ontologyIRI = IRI.create(OntologyUtils.getNamespace(classIri));
			clearManagerFromOntologies(ontologyIRI);

			destinationOntology = manager.createOntology(ontologyIRI);

			OWLClassExpression resultExpression = getClassRestrictionGenerator().convertToOntClass(classUri, condition);

			addToOntologyAsClass(destinationOntology, resultExpression, classUri);
			return destinationOntology;

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return null;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String convertToOwlClass(String classUri, ClassCondition condition) {
		OWLOntology destinationOntology = convertToOwlClassOntology(classUri, condition);

		try {
			return serializeToString(destinationOntology);
		} catch (OWLOntologyStorageException e) {

			System.out.println("Error at String convertToOwlClass: " + e.getMessage());
			return null;
		}
	}

	public OWLOntology convertToOwlIndividualOntology(String individualUri, ClassCondition condition) {
		OWLOntology destinationOntology;
		try {
			IRI iri = IRI.create(individualUri);
			IRI ontologyIRI = IRI.create(OntologyUtils.getNamespace(iri));
			clearManagerFromOntologies(ontologyIRI);
			destinationOntology = manager.createOntology(ontologyIRI);

			List<OWLAxiom> axioms = getIndividualGenerator().convertToOntIndividual(individualUri, condition);

			addToOntologyAsIndividualDescription(destinationOntology, axioms);
			addImportDeclarations(destinationOntology, axioms);
			return destinationOntology;

		} catch (OWLOntologyCreationException e) {
			System.out.println("Error at OWLOntology convertToOwlIndividualOntology: " + e.getMessage());
			return null;
		} catch (ConfigurationException e) {
			System.out.println("Error at OWLOntology convertToOwlIndividualOntology: " + e.getMessage());
			return null;
		}
	}

	public String convertToOwlIndividual(String individualUri, ClassCondition condition) {
		OWLOntology destinationOntology = convertToOwlIndividualOntology(individualUri, condition);

		try {
			return serializeToString(destinationOntology);
		} catch (OWLOntologyStorageException e) {
			System.out.println("Error at String convertToOwlIndividual: " + e.getMessage());
			return null;
		}
	}

	private void addImportDeclarations(OWLOntology destinationOntology, List<OWLAxiom> axioms) {

		for (OWLAxiom axiom : axioms) {
			for (OWLEntity ent : axiom.getSignature()) {

				IRI ontoIRI = IRI.create(OntologyUtils.getNamespace(ent.getIRI()));

				OWLImportsDeclaration importsDeclaration = factory.getOWLImportsDeclaration(ontoIRI);
				if (!ontoIRI.toString().contains("XMLSchema")
						&& !destinationOntology.getOntologyID().getOntologyIRI().equals(ontoIRI)
						&& !destinationOntology.getImportsDeclarations().contains(importsDeclaration)) {
					AddImport addImportChange = new AddImport(destinationOntology, importsDeclaration);
					manager.applyChange(addImportChange);
				}
			}
		}
	}

	private void addToOntologyAsIndividualDescription(OWLOntology destinationOntology, List<OWLAxiom> axioms) {
		for (OWLAxiom owlAxiom : axioms) {
			manager.addAxiom(destinationOntology, owlAxiom);
		}
	}

	private void addToOntologyAsClass(OWLOntology destinationOntology, OWLClassExpression resultExpression,
			String conditionUri) {
		OWLClass resultClass = factory.getOWLClass(IRI.create(conditionUri));

		OWLAxiom equivalentClassAxiom = 
				factory.getOWLEquivalentClassesAxiom(resultClass, resultExpression);
		manager.addAxiom(destinationOntology, equivalentClassAxiom);
	}

	private String serializeToString(OWLOntology destinationOntology) throws OWLOntologyStorageException {
		return serializeToString(destinationOntology, true);
	}

	private String serializeToString(OWLOntology destinationOntology, boolean useRdf)
			throws OWLOntologyStorageException {
		StringDocumentTarget serializationTarget = new StringDocumentTarget();
		if (useRdf) {
			RDFXMLOntologyFormat rdfXmlFormat = new RDFXMLOntologyFormat();
			manager.saveOntology(destinationOntology, rdfXmlFormat, serializationTarget);
		} else {
			OWLXMLOntologyFormat owlXmlFormat = new OWLXMLOntologyFormat();
			manager.saveOntology(destinationOntology, owlXmlFormat, serializationTarget);
		}
		return serializationTarget.toString();
	}

	/**
	 * remove ontology if it exists in the manager. When adding for the second
	 * time, I got an error that ontology already exist. That's because I don't
	 * create new object of the ontology Generator.
	 * 
	 * @param ontologyIRI
	 *            IRI for the ontology to be removed.
	 */
	private void clearManagerFromOntologies(IRI ontologyIRI) {
		for (OWLOntology managerOwlOntology : manager.getOntologies()) {
			if (managerOwlOntology.getOntologyID().getOntologyIRI().equals(ontologyIRI)) {
				manager.removeOntology(managerOwlOntology);
			}
		}
	}
	
	public boolean removeIndividual(String individualUri){
		OWLNamedIndividual individual=factory.getOWLNamedIndividual(IRI.create(individualUri));
		if(individual == null)
			return false;
		OWLEntityRemover remover = new OWLEntityRemover(manager,manager.getOntologies());
		individual.accept(remover);
		manager.applyChanges(remover.getChanges());	
		OutputStream out;
		try {
			out = new FileOutputStream(ontoplay.controllers.utils.OntologyUtils.fileName);
	
		manager.saveOntology(manager.getOntology(IRI.create(ontoplay.controllers.utils.OntologyUtils.nameSpace)), out);
		out.close();
		return false;
		} catch (Exception e) {
			return false;
		}
	}

}