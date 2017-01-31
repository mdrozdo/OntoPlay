package ontoplay.jobs;

import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;

public class JenaOwlReaderConfiguration{
	
	public void doJob() throws Exception {
		String uri = "file:../../samples/GridSample/Ontology/AiGGridInstances.owl";
		JenaOwlReaderConfig jenaOwlReaderConfig = new JenaOwlReaderConfig()
			.useLocalMapping("http://gridagents.sourceforge.net/AiGGridOntology", "file:../../samples/GridSample/Ontology/AiGGridOntology.owl")
			.useLocalMapping("http://www.w3.org/2006/time", "file:../../samples/GridSample/Ontology/time.owl")
			.useLocalMapping("http://purl.org/NET/cgo", "file:../../samples/GridSample/Ontology/cgo.owl")
			.useLocalMapping("http://purl.org/NET/cgoInstances", "file:../../samples/GridSample/Ontology/cgo.owl")
			.ignorePropertiesWithUnspecifiedDomain();
		
		initialize(uri, jenaOwlReaderConfig);
	}

	public void initialize(String uri, JenaOwlReaderConfig jenaOwlReaderConfig) {
		JenaOwlReader.initialize(uri,
				jenaOwlReaderConfig);
		
	}
}
