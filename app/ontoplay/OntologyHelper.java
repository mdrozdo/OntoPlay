package ontoplay;

import jadeOWL.base.OntologyManager;
import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.OntologyReaderFactory;
import ontoplay.models.ontologyReading.jena.FolderMapping;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class OntologyHelper {
	private final OntologyReader ontoReader;
	private final OntologyReaderFactory ontoReaderFactory;
	private final OntoplayConfig config;

	@Inject
    public OntologyHelper(
			OntoplayConfig config,
			OntologyReader ontoReader,
			OntologyReaderFactory ontoReaderFactory) {
        this.ontoReader = ontoReader;
		this.ontoReaderFactory = ontoReaderFactory;
		this.config = config;
    }

    public boolean saveOntology(OWLOntology generatedOntology) {
		OWLOntologyManager OWLmanager = OWLManager.createOWLOntologyManager();
		OWLOntology originalOntology = null;

		String filePath = config.getOntologyFilePath();
		String ontologyNamespace = ontoReader.getOntologyNamespace();

		try {
			originalOntology = OWLmanager
					.loadOntologyFromOntologyDocument(new File(filePath));
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OntologyManager manager = new OntologyManager();
		IRI test = IRI.create(ontologyNamespace);
		OWLOntology newOntology = null;

		try {
			newOntology = manager.mergeOntologies(test, originalOntology,generatedOntology);
		} catch (org.semanticweb.owlapi.model.OWLOntologyCreationException
				| OWLOntologyStorageException | IOException e) {
			// TODO Auto-generated catch block
			System.out.print("error");
			e.printStackTrace();
		}

		OutputStream out = null;
		try {
				out = new FileOutputStream(filePath);
				OWLmanager.saveOntology(newOntology, out);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				try
				{
				if(out!=null)
					out.close();
				}
				catch(Exception ex){
					
				}
				e.printStackTrace();
				}
		return true;
	}

	public boolean checkOntology(OWLOntology generatedOntology) {
		try {
			OWLOntologyManager OWLmanager = OWLManager.createOWLOntologyManager();
			OWLOntology originalOntology = null;

			String filePath = config.getOntologyFilePath();
			String ontologyNamespace = ontoReader.getOntologyNamespace();

			try {
				originalOntology = OWLmanager.loadOntologyFromOntologyDocument(new File(filePath));
			} catch (OWLOntologyCreationException e1) {
				// TODO Auto-generated catch block
				return false;
			}

			OntologyManager manager = new OntologyManager();
			IRI test = IRI.create(ontologyNamespace);
			OWLOntology newOntology = null;
			try {
				newOntology = manager.mergeOntologies(test, originalOntology,generatedOntology);
			} catch (org.semanticweb.owlapi.model.OWLOntologyCreationException
					| OWLOntologyStorageException | IOException e) {
				// TODO Auto-generated catch block
				return false;
			}

			String checkFilePath = config.getCheckFilePath();

			try {
				OutputStream out = new FileOutputStream(checkFilePath);
				//out.write(5);
				OWLmanager.saveOntology(newOntology, out);
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				return false;
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public OntologyReader checkOwlReader() {
		String ontologyNamespace = ontoReader.getOntologyNamespace();
		String checkFilePath = config.getCheckFilePath();
		List<FolderMapping> mappings = Arrays.asList(new FolderMapping(ontologyNamespace, checkFilePath));
		return ontoReaderFactory.create("file://"+ checkFilePath, mappings, false);
	}

	public OntoClass getOwlClass(String classUri) {
        return ontoReader.getOwlClass(classUri);
    }

    public OntoProperty getProperty(String propertyUri) throws ConfigurationException {
        return ontoReader.getProperty(propertyUri);
    }
}
