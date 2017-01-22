package ontoplay;

import jadeOWL.base.OntologyManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.OntologyReaderFactory;
import ontoplay.models.ontologyReading.jena.FolderMapping;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.inject.Inject;
import javax.inject.Named;

public class OntologyHelper {
	private String ontologyName;
    private OntologyReader ontoReader;
	private OntologyReaderFactory ontoReaderFactory;

	private String file;
	private String filePath;
	private String checkFile;
	private String checkFilePath;
    private String ontologyNamespace;

	@Inject
    public OntologyHelper(
			@Named("ontoplay.fileName") String fileName,
			@Named("ontoplay.folderPath") String folderPath,
			@Named("ontoplay.checkFileName") String checkFileName,
			@Named("ontoplay.checkFolderPath") String checkFolderPath,
			@Named("ontoplay.ontologyNamespace") String ontologyNamespace,
			OntologyReader ontoReader,
			OntologyReaderFactory ontoReaderFactory) {
        this.ontologyName = fileName;
        this.ontoReader = ontoReader;
		this.ontoReaderFactory = ontoReaderFactory;
		this.file = "file:" + folderPath + "/" + fileName;
        this.filePath = folderPath + "/" + fileName;
        this.checkFile = "file:" + checkFolderPath + "/" + checkFileName;
        this.checkFilePath =  checkFolderPath + "/" + checkFileName;
        this.ontologyNamespace = ontologyNamespace;
    }

    public boolean saveOntology(OWLOntology generatedOntology) {
		OWLOntologyManager OWLmanager = OWLManager.createOWLOntologyManager();
		OWLOntology originalOntology = null;
	
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
		List<FolderMapping> mappings = Arrays.asList(new FolderMapping(ontologyNamespace, checkFilePath));
		return ontoReaderFactory.create(checkFile, mappings, false);
	}
	
	public String getComponentIriByName(String name){
		return ontologyNamespace +"#"+name;
	}

    public OntoClass getOwlClass(String classUri) {
        return ontoReader.getOwlClass(classUri);
    }

    public OntoProperty getProperty(String propertyUri) throws ConfigurationException {
        return ontoReader.getProperty(propertyUri);
    }
}
