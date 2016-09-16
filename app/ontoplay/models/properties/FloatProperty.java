package ontoplay.models.properties;

import ontoplay.models.Constants;

public class FloatProperty extends OwlDatatypeProperty {

	public FloatProperty(String namespace, String localName,String label) {
		super(namespace, localName, Constants.FLOAT_MAIN_RANGE,label);
	}

	public FloatProperty(String namespace, String localName, String datatypeUri,String label) {
		super(namespace, localName, datatypeUri,label);
	}
}
