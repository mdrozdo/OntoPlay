package ontoplay.jobs;

import ontoplay.models.owlGeneration.OntologyGenerator;

public class OntologyGeneratorConfiguration {
	
	public void doJob() throws Exception {
		OntologyGenerator.initialize("file:../../samples/GridSample/Ontology/AiGGridInstances.owl", "../../samples/GridSample/Ontology");
	}
}
