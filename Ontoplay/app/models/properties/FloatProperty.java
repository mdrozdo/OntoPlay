package models.properties;

public class FloatProperty extends OwlDatatypeProperty {

	public FloatProperty(String namespace, String localName) {
		super(namespace, localName, "http://www.w3.org/2001/XMLSchema#float");
	}

	public FloatProperty(String namespace, String localName, String datatypeUri) {
		super(namespace, localName, datatypeUri);
	}
}
