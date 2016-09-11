package ontoplay.models.properties;

import ontoplay.models.Constants;

public class StringProperty extends OwlDatatypeProperty{

	public StringProperty(String namespace, String localName,String label) {
		this(namespace, localName, Constants.STRING_MAIN_RANGE,label);
	}
	

	public StringProperty(String namespace, String localName, String datatype,String label) {
		super(namespace, localName, datatype,label);
		
	}

	
}
