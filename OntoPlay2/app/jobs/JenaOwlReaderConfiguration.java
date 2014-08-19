package jobs;

import models.ontologyReading.jena.JenaOwlReader;
import models.ontologyReading.jena.JenaOwlReaderConfig;
import models.ontologyReading.jena.OwlPropertyFactory;
import models.ontologyReading.jena.propertyFactories.DateTimePropertyFactory;
import models.ontologyReading.jena.propertyFactories.FloatPropertyFactory;
import models.ontologyReading.jena.propertyFactories.IntegerPropertyFactory;
import models.ontologyReading.jena.propertyFactories.ObjectPropertyFactory;
import models.ontologyReading.jena.propertyFactories.StringPropertyFactory;

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
		
		OwlPropertyFactory.registerPropertyFactory(new IntegerPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new FloatPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new DateTimePropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new StringPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new ObjectPropertyFactory());
	}
}
