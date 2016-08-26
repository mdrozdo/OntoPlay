package models.properties;

import models.ontologyModel.OntoProperty;

public class OwlDatatypeProperty implements OntoProperty{
	private String namespace;
	private String localName;
	private String datatype;

	public OwlDatatypeProperty(String namespace, String localName, String datatype) {
		this.namespace = namespace;
		this.localName = localName;
		this.datatype = datatype;
	}

	public String getDatatype() {
		return datatype;
	}

	public String getLocalName() {
		return localName;
	}

	public String getUri(){
		return String.format("%s%s", namespace, localName);
	}
}
