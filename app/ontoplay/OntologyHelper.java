package ontoplay;

import jadeOWL.base.OntologyManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import ontoplay.jobs.JenaOwlReaderConfiguration;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class OntologyHelper {
	
	public static String ontologyName="TAN.OWL";

	public static String file = "file:"+Pathes.UPLOADS_PATH+ontologyName;

	public static String fileName = Pathes.UPLOADS_PATH+ontologyName;
	
	public static String checkFile = "file:samples/TAN/TANCheckk.owl";
	
	public static String checkFileName = "./samples/TAN/TANCheckk.owl";
	

	public static String checkFilePath = "./samples/OrganizationCheck";

	public static String nameSpace = "http://www.tan.com#";

	public static String iriString = "http://www.tan.com";

	public static boolean saveOntology(OWLOntology generatedOntology) {
		OWLOntologyManager OWLmanager = OWLManager.createOWLOntologyManager();
		OWLOntology originalOntology = null;
	
		try {
			originalOntology = OWLmanager
					.loadOntologyFromOntologyDocument(new File(fileName));
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OntologyManager manager = new OntologyManager();
		IRI test = IRI.create(iriString);
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
				out = new FileOutputStream(fileName);
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

	public static boolean checkOntology(OWLOntology generatedOntology) {
		try {
			OWLOntologyManager OWLmanager = OWLManager.createOWLOntologyManager();
			OWLOntology originalOntology = null;
			try {
				originalOntology = OWLmanager.loadOntologyFromOntologyDocument(new File(fileName));
			} catch (OWLOntologyCreationException e1) {
				// TODO Auto-generated catch block
				return false;
			}

			OntologyManager manager = new OntologyManager();
			IRI test = IRI.create(iriString);
			OWLOntology newOntology = null;
			try {
				newOntology = manager.mergeOntologies(test, originalOntology,generatedOntology);
			} catch (org.semanticweb.owlapi.model.OWLOntologyCreationException
					| OWLOntologyStorageException | IOException e) {
				// TODO Auto-generated catch block
				return false;
			}

			try {
				OutputStream out = new FileOutputStream(checkFileName);
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

	public static OntologyReader checkOwlReader() {
		new JenaOwlReaderConfiguration().
		initialize(checkFile, new JenaOwlReaderConfig().useLocalMapping(iriString,checkFileName));
		return OntologyReader.getGlobalInstance();
	}
	
	public static String getComponentIriByName(String name){
		return nameSpace+name;
	}
	
}
