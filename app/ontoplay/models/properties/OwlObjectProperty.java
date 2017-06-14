package ontoplay.models.properties;

import org.apache.jena.ontology.Individual;

import ontoplay.models.ontologyModel.OntoProperty;

public class OwlObjectProperty implements OntoProperty {

	private String namespace;
	private String localName;
	private String label;

	public OwlObjectProperty(String namespace, String localName, String label) {
		this.namespace = namespace;
		this.localName = localName;
		this.label=label;
	}

	@Override
	public String getLocalName() {
		return localName;
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getUri(){
		return String.format("%s%s", namespace, localName);
	}

}
