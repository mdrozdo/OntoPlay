package controllers.samples;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import controllers.OntologyController;

import jobs.JenaOwlReaderConfiguration;

import models.ClassCondition;
import models.ConditionDeserializer;
import models.JadeOwlMessageReader;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OwlIndividual;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;
import models.owlGeneration.OntologyGenerator;
import play.mvc.*;
import views.html.samples.*;
import play.data.Form;


public class JobExecution extends OntologyController {

	private static JadeOwlMessageReader jadeOwl = JadeOwlMessageReader.getGlobalInstance();
	private static OntologyGenerator owlApi = OntologyGenerator.getGlobalInstance();

	// private static Map<String, Reply> latestReplies = new HashMap<String, Reply>();

	private final static String CGO_NS = "http://purl.org/NET/cgo#";

	public static Result index() {
		try {
			String uri = "file:samples/GridSample/Ontology/AiGGridInstances.owl";
			JenaOwlReaderConfig jenaOwlReaderConfig = new JenaOwlReaderConfig()
				.useLocalMapping("http://gridagents.sourceforge.net/AiGGridOntology", "file:samples/GridSample/Ontology/AiGGridOntology.owl")
				.useLocalMapping("http://www.w3.org/2006/time", "file:samples/GridSample/Ontology/time.owl")
				.useLocalMapping("http://purl.org/NET/cgoInstances", "file:samples/GridSample/Ontology/cgoInstances.owl")
				.useLocalMapping("http://purl.org/NET/cgo", "file:samples/GridSample/Ontology/cgo.owl")
				.ignorePropertiesWithUnspecifiedDomain();
			
			new JenaOwlReaderConfiguration().initialize(uri, jenaOwlReaderConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		OntoClass owlClass = getOntologyReader().getOwlClass(CGO_NS + "ComputingElement");
		return ok(jobexecution.render(owlClass));
	}

	public static Result updateConditions() {
		String conditionJson = Form.form().bindFromRequest().get("conditionJson");
		ClassCondition condition = ConditionDeserializer.deserializeCondition(getOntologyReader(), conditionJson);
		String conditionRdf = owlApi.convertToOwlClass("http://gridagents.sourceforge.net/TeamConditions#TeamCondition", condition);

		return ok(conditionRdf);
	}

	public static Result updateSuccessful(String condition) {
		return ok("updateSuccessful");
	}

}
