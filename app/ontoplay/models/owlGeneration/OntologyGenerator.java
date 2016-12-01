package ontoplay.models.owlGeneration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ontoplay.models.ClassCondition;
import ontoplay.models.ConfigurationException;
import ontoplay.models.OntologyUtils;
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
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.inject.Singleton;

@Singleton
public class OntologyGenerator {
	private OWLDataFactory factory;
	private OWLOntologyManager manager;
	
	private ClassRestrictionGenerator classRestrictionGenerator;
	private IndividualGenerator individualGenerator;

	public ClassRestrictionGenerator getClassRestrictionGenerator() {
		return classRestrictionGenerator;
	}
	
	public IndividualGenerator getIndividualGenerator() {
		return individualGenerator;
	}
	
	public OWLDataFactory getOwlApiFactory(){
		return factory;
	}

	public OntologyGenerator(ClassRestrictionGeneratorFactory classGenFactory, IndividualGeneratorFactory individualGenFactory){
		//Should these be injected?
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();

		//Should these be created from a Guice factory?
		classRestrictionGenerator = classGenFactory.create(factory);
		individualGenerator = individualGenFactory.create(factory);
	}

	public OWLOntology convertToOwlClassOntology(String classUri, ClassCondition condition) {
		OWLOntology destinationOntology;
		try {
			IRI iri = IRI.create(classUri);
			
			String ontologyIRI = OntologyUtils.getNamespace(iri);
			
			destinationOntology = manager.createOntology(IRI.create(ontologyIRI));
			
			OWLClassExpression resultExpression = getClassRestrictionGenerator().convertToOntClass(classUri, condition);
			
			//addImportDeclarations(destinationOntology, resultExpression);
			addToOntologyAsClass(destinationOntology, resultExpression, classUri);
			
			return destinationOntology;
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;		
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}			
	}

    public String convertToOwlClass(String classUri, ClassCondition condition) {
        OWLOntology destinationOntology = convertToOwlClassOntology(classUri, condition);

        try {
            return serializeToString(destinationOntology);
        } catch (OWLOntologyStorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

	public OWLOntology convertToOwlIndividualOntology(String individualUri, ClassCondition condition) {
		OWLOntology destinationOntology;
		try {
			IRI iri = IRI.create(individualUri);
			String ontologyIRI = OntologyUtils.getNamespace(iri);
			
			destinationOntology = manager.createOntology(IRI.create(ontologyIRI));

			List<OWLAxiom> axioms = getIndividualGenerator().convertToOntIndividual(individualUri, condition);
			
			addToOntologyAsIndividualDescription(destinationOntology, axioms);
			addImportDeclarations(destinationOntology, axioms);
			return destinationOntology;
			
		} catch (OWLOntologyCreationException e) {
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
		OWLOntology destinationOntology = convertToOwlIndividualOntology(individualUri, condition);

		try {
            return serializeToString(destinationOntology);
        } catch (OWLOntologyStorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }						
	}

	private void addImportDeclarations(OWLOntology destinationOntology,
			List<OWLAxiom> axioms) {
		
		for (OWLAxiom axiom : axioms) {
			for(OWLEntity ent : axiom.getSignature()){
				
				IRI ontoIRI = IRI.create(OntologyUtils.getNamespace(ent.getIRI()));
				
				OWLImportsDeclaration importsDeclaration = factory.getOWLImportsDeclaration(ontoIRI);
				if(!ontoIRI.toString().contains("XMLSchema")
						&& !destinationOntology.getOntologyID().getOntologyIRI().equals(ontoIRI) 
						&& !destinationOntology.getImportsDeclarations().contains(importsDeclaration)){
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

	private void addToOntologyAsClass(OWLOntology destinationOntology, OWLClassExpression resultExpression, String conditionUri) {
		OWLClass resultClass = factory.getOWLClass(IRI.create(conditionUri));
		
		OWLAxiom equivalentClassAxiom = factory.getOWLEquivalentClassesAxiom(resultClass, resultExpression);
		addImportDeclarations(destinationOntology, new ArrayList<OWLAxiom>(
				Arrays.asList(new OWLAxiom[] { equivalentClassAxiom })));
		
		manager.addAxiom(destinationOntology, equivalentClassAxiom);
	}

	private String serializeToString(OWLOntology destinationOntology) throws OWLOntologyStorageException {
		return serializeToString(destinationOntology, true);
	}
			
	private String serializeToString(OWLOntology destinationOntology, boolean useRdf) throws OWLOntologyStorageException {
		StringDocumentTarget serializationTarget = new StringDocumentTarget();
		if(useRdf){
			RDFXMLOntologyFormat rdfXmlFormat = new RDFXMLOntologyFormat();
			manager.saveOntology(destinationOntology, rdfXmlFormat, serializationTarget);				
		}
		else{
			OWLXMLOntologyFormat owlXmlFormat = new OWLXMLOntologyFormat();
			manager.saveOntology(destinationOntology, owlXmlFormat, serializationTarget);
		}
		return serializationTarget.toString();
	}

	

	
}