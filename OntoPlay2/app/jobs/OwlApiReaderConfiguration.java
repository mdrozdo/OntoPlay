package jobs;

import org.semanticweb.owlapi.model.OWLOntology;

import models.ontologyReading.owlApi.OwlPropertyFactory;
import models.ontologyReading.owlApi.propertyFactories.DateTimePropertyFactory;
import models.ontologyReading.owlApi.propertyFactories.FloatPropertyFactory;
import models.ontologyReading.owlApi.propertyFactories.IntegerPropertyFactory;
import models.ontologyReading.owlApi.propertyFactories.ObjectPropertyFactory;
import models.ontologyReading.owlApi.propertyFactories.StringPropertyFactory;
import models.ontologyReading.owlApi.OwlApiReader;
import models.owlGeneration.OntologyGenerator;

public class OwlApiReaderConfiguration  {
	public void doJob() throws Exception {
		initialize("test/AiGExpertOntology.owl", "./test");
	}
	
	public void initialize(String uri, String localOntologyFolder){
		OwlApiReader.initialize(uri, localOntologyFolder);
		
		OwlPropertyFactory.registerPropertyFactory(new IntegerPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new FloatPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new DateTimePropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new StringPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new ObjectPropertyFactory());	
	}
}
