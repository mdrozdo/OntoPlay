package models.properties;

public class FloatProperty extends OwlDatatypeProperty {

	public FloatProperty(String namespace, String localName,String label) {
		super(namespace, localName, "http://www.w3.org/2001/XMLSchema#float",label);
	}

	public FloatProperty(String namespace, String localName, String datatypeUri,String label) {
		super(namespace, localName, datatypeUri,label);
	}
}
