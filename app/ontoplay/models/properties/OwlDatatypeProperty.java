package ontoplay.models.properties;

import org.apache.jena.ontology.Individual;

import ontoplay.models.ontologyModel.OntoProperty;

public class OwlDatatypeProperty implements OntoProperty{
	private String namespace;
	private String localName;
	private String datatype;
	private String label;

	public OwlDatatypeProperty(String namespace, String localName, String datatype,String label) {
		this.namespace = namespace;
		this.localName = localName;
		this.datatype = datatype;
		this.label=label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public String getDatatype() {
		return datatype;
	}

	@Override
	public String getLocalName() {
		return localName;
	}

	@Override
	public String getUri(){
		return String.format("%s%s", namespace, localName);
	}
}
