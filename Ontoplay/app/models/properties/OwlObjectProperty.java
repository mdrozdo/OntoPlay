package models.properties;

import models.ontologyModel.OntoProperty;

public class OwlObjectProperty implements OntoProperty {

	private String namespace;
	private String localName;

	public OwlObjectProperty(String namespace, String localName) {
		this.namespace = namespace;
		this.localName = localName;
	}

	public String getLocalName() {
		return localName;
	}

	public String getUri(){
		return String.format("%s%s", namespace, localName);
	}

}
