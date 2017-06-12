package ontoplay.controllers.utils;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ontoplay.OntoplayConfig;
import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.OntologyReaderFactory;
import ontoplay.models.ontologyReading.jena.FolderMapping;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import uk.ac.manchester.cs.owl.owlapi.OWLOntologyIRIMapperImpl;

import javax.inject.Inject;

public class OntologyUtils {

	private final OWLOntologyManager owlManager;
	private OntoplayConfig config;
	private OntologyReader ontoReader;
	private OntologyReaderFactory ontoReaderFactory;

	@Inject
	public OntologyUtils(OntoplayConfig config,
		OntologyReader ontoReader,
		OntologyReaderFactory ontoReaderFactory) {
		this.config = config;
		this.ontoReader = ontoReader;
		this.ontoReaderFactory = ontoReaderFactory;
		this.owlManager = OWLManager.createOWLOntologyManager();
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
			System.out.print("Error at OntologyUtils.saveOntology 47" + e1.getMessage());
			e1.printStackTrace();
		}

		OWLOntology newOntology = null;

		try {
			newOntology = mergeOntologies(generatedOntology.getOntologyID().getOntologyIRI(), originalOntology, generatedOntology);
		} catch (org.semanticweb.owlapi.model.OWLOntologyCreationException | OWLOntologyStorageException
				| IOException e) {
			System.out.print("Error at OntologyUtils.saveOntology 58 " + e.getMessage());
			e.printStackTrace();
		}

		OutputStream out = null;
		try {
			OWLOntologyFormat originalFormat = owlManager.getOntologyFormat(originalOntology);
			out = new FileOutputStream(config.getOntologyFilePath());
			owlManager.saveOntology(newOntology, originalFormat, out);
		} catch (Exception e) {
			System.out.print("Error at OntologyUtils.saveOntology 66" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					System.out.print("Error at OntologyUtils.saveOntology 73" + e.getMessage());
				}
			}
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

			IRI test = IRI.create(ontologyNamespace);
			OWLOntology newOntology = null;
			try {
				newOntology = mergeOntologies(test, originalOntology,generatedOntology);
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


	public String nameToUri(String name, String ontologyNamespace) {
		if (name.contains("#")) {
			return name;
		} else {
			return joinNamespaceAndName(ontologyNamespace,name);
		}
	}

	/**
	 * Merges two or more ontologies into one and saves it with a specified iri
	 * @param mergedOntologyIRI
	 * @param ontologies
	 * @return
	 * @throws OWLOntologyCreationException
	 * @throws OWLOntologyStorageException
	 * @throws IOException
	 */
	public OWLOntology mergeOntologies(IRI mergedOntologyIRI, OWLOntology ... ontologies) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		if (ontologies.length < 2)
			throw new IllegalArgumentException("There were not enough ontologies to be merged." +
					"Please, give at least 2 ontologies as parameters");
		//NEW CODE
		OWLOntology mergedOntology = owlManager.createOntology(mergedOntologyIRI);
		for (OWLOntology ontology : ontologies) {
			owlManager.addAxioms(mergedOntology, ontology.getAxioms());
			Set<OWLOntology> imports = ontology.getImports();
			for (Iterator<OWLOntology> it = imports.iterator(); it.hasNext(); ) {
				OWLOntology onto = it.next();
				addImportToOntology(mergedOntology, onto.getOntologyID().getOntologyIRI());
			}
		}
		return mergedOntology;
	}

	public boolean addImportToOntology(OWLOntology ontology, IRI importIRI) {
		if(importIRI == null) {
			return false;
		} else {
			OWLImportsDeclaration imports = owlManager.getOWLDataFactory().getOWLImportsDeclaration(importIRI);
			AddImport addImport = new AddImport(ontology, imports);
			owlManager.applyChange(addImport);
			return true;
		}
	}

}
