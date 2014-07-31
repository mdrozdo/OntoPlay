package jobs;

import models.JadeOwlMessageReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;


//@OnApplicationStart
public class JadeOwlConfiguration {
	
	public void doJob() throws Exception {
		JadeOwlMessageReader.initialize(new JenaOwlReaderConfig()
					.useLocalMapping("http://gridagents.sourceforge.net/AiGGridOntology", "file:test/AiGGridOntology.owl")
					.useLocalMapping("http://www.w3.org/2006/time", "file:test/time.owl")
					.useLocalMapping("http://www.owl-ontologies.com/unnamed.owl", "file:test/cgo.owl"));
	}
}
