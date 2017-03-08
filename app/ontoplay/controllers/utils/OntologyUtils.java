package ontoplay.controllers.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;

import ontoplay.OntoplayConfig;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import jadeOWL.base.OntologyManager;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;

import javax.inject.Inject;

public class OntologyUtils {

//	public String getFile = "file:" + PathesUtils.UPLOADS_PATH + ontologyName;
//
//	public static String fileName = PathesUtils.UPLOADS_PATH + ontologyName;
//
//	public static String checkFile = "file:" + PathesUtils.UPLOADS_PATH + "Check" + ontologyName;
//
//	public static String checkFileName = PathesUtils.UPLOADS_PATH + "Check" + ontologyName;
//
//	public static String nameSpace = "";
//
//	public static String iriString = "";
	private OntoplayConfig config;

	@Inject
	public OntologyUtils(OntoplayConfig config){

		this.config = config;
	}

	public boolean saveOntology(OWLOntology generatedOntology) {
		OWLOntologyManager OWLmanager = OWLManager.createOWLOntologyManager();
		OWLOntology originalOntology = null;

		try {
			originalOntology = OWLmanager.loadOntologyFromOntologyDocument(new File(config.getOntologyFilePath()));
		} catch (OWLOntologyCreationException e1) {
			System.out.print("Error at OntologyHelper.saveOntology 47" + e1.getMessage());
			e1.printStackTrace();
		}

		OntologyManager manager = new OntologyManager();
		OWLOntology newOntology = null;

		try {
			newOntology = manager.mergeOntologies(generatedOntology.getOntologyID().getOntologyIRI(), originalOntology, generatedOntology);
		} catch (org.semanticweb.owlapi.model.OWLOntologyCreationException | OWLOntologyStorageException
				| IOException e) {
			System.out.print("Error at OntologyHelper.saveOntology 58 " + e.getMessage());
			e.printStackTrace();
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream(config.getOntologyFilePath());
			OWLmanager.saveOntology(newOntology, out);
		} catch (Exception e) {
			System.out.print("Error at OntologyHelper.saveOntology 66" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					System.out.print("Error at OntologyHelper.saveOntology 73" + e.getMessage());
				}
			}
		}
		return true;
	}
}
