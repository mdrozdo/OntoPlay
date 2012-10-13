package jobs;

import models.JadeOwlMessageReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class JadeOwlConfiguration extends Job {
	@Override
	public void doJob() throws Exception {
		JadeOwlMessageReader.initialize(new JenaOwlReaderConfig()
					.useLocalMapping("http://gridagents.sourceforge.net/AiGGridOntology", "file:test/AiGGridOntology.owl")
					.useLocalMapping("http://www.w3.org/2006/time", "file:test/time.owl")
					.useLocalMapping("http://www.owl-ontologies.com/unnamed.owl", "file:test/cgo.owl"));
		super.doJob();
	}
}
