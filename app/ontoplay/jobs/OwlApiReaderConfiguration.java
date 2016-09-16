package ontoplay.jobs;

import ontoplay.models.ontologyReading.owlApi.OwlApiReader;

public class OwlApiReaderConfiguration  {
	public void doJob() throws Exception {
		initialize("file:../../samples/GridSample/Ontology/AiGGridInstances.owl", "../../samples/GridSample/Ontology");
	}
	
	public void initialize(String uri, String localOntologyFolder){
		OwlApiReader.initialize(uri, localOntologyFolder);
	}
}
