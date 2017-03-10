package ontoplay.controllers.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ontoplay.OntoplayConfig;
import ontoplay.models.ontologyReading.jena.FolderMapping;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import jadeOWL.base.OntologyManager;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyIRIMapperImpl;

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

	public String joinNamespaceAndName(String namespace, String name){
		return String.format("%s#%s", namespace, name);
	}

	public boolean saveOntology(OWLOntology generatedOntology) {
		OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
		OWLOntology originalOntology = null;

		try {
			OWLOntologyIRIMapperImpl iriMapper = new OWLOntologyIRIMapperImpl();
			for (FolderMapping mapping : config.getMappings()) {
				iriMapper.addMapping(IRI.create(mapping.getUri()), IRI.create(mapping.getFolderPath()));
			}
			owlManager.addIRIMapper(iriMapper);

			originalOntology = owlManager.loadOntologyFromOntologyDocument(new File(config.getOntologyFilePath()));

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
			OWLOntologyFormat originalFormat = owlManager.getOntologyFormat(originalOntology);
			out = new FileOutputStream(config.getOntologyFilePath());
			owlManager.saveOntology(newOntology, originalFormat, out);
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

	public String nameToUri(String name, String ontologyNamespace) {
		if (name.contains("#")) {
			return name;
		} else {
			return joinNamespaceAndName(ontologyNamespace,name);
		}
	}
}
