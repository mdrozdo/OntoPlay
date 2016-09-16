package ontoplay.models.properties;

import ontoplay.models.Constants;

public class IntegerProperty extends OwlDatatypeProperty {
	
	public IntegerProperty(String namespace, String localName, String datatype,String label) {
		super(namespace, localName, datatype,label);
	}

	public IntegerProperty(String namespace, String localName,String label) {
		this(namespace, localName, Constants.INTEGER_MAIN_RANGE,label);
	}
}
