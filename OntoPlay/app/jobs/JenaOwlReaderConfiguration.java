package jobs;

import models.ontologyReading.jena.JenaOwlReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;
import models.ontologyReading.jena.OwlPropertyFactory;
import models.ontologyReading.jena.propertyFactories.DateTimePropertyFactory;
import models.ontologyReading.jena.propertyFactories.FloatPropertyFactory;
import models.ontologyReading.jena.propertyFactories.IntegerPropertyFactory;
import models.ontologyReading.jena.propertyFactories.ObjectPropertyFactory;
import models.ontologyReading.jena.propertyFactories.StringPropertyFactory;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

public class JenaOwlReaderConfiguration extends Job {
	@Override
	public void doJob() throws Exception {
		String uri = "file:test/AiGConditionsOntology.owl";
		JenaOwlReaderConfig jenaOwlReaderConfig = new JenaOwlReaderConfig()
			.useLocalMapping("http://gridagents.sourceforge.net/AiGGridOntology", "file:test/AiGGridOntology.owl")
			.useLocalMapping("http://www.w3.org/2006/time", "file:test/time.owl")
			.useLocalMapping("http://www.owl-ontologies.com/unnamed.owl", "file:test/cgo.owl")
			.ignorePropertiesWithUnspecifiedDomain();
		
		initialize(uri, jenaOwlReaderConfig);
		
		super.doJob();
	}

	public void initialize(String uri, JenaOwlReaderConfig jenaOwlReaderConfig) {
		JenaOwlReader.initialize(uri,
				jenaOwlReaderConfig);
		
		OwlPropertyFactory.registerPropertyFactory(new IntegerPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new FloatPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new DateTimePropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new StringPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new ObjectPropertyFactory());
	}
}
