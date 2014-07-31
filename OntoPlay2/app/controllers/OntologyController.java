package controllers;

import jobs.JenaOwlReaderConfiguration;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;
import play.mvc.Controller;

public class OntologyController extends Controller {

	private static OntologyReader ontologyReader;

	protected static OntologyReader getOntologyReader() {
		if(ontologyReader == null){
			
			ontologyReader = OntologyReader.getGlobalInstance();
		}
		
		return ontologyReader;
	}

	public OntologyController() {
		super();
	}

}