package ontoplay.models.ontologyModel;

import org.apache.jena.ontology.Individual;


public interface OwlElement {
	public String getUri();
	public String getLocalName();
	public String getLabel();
	public Individual getIndividual();
}
