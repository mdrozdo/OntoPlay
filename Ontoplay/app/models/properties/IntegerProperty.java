package models.properties;

public class IntegerProperty extends OwlDatatypeProperty {
	
	public IntegerProperty(String namespace, String localName, String datatype) {
		super(namespace, localName, datatype);
	}

	public IntegerProperty(String namespace, String localName) {
		this(namespace, localName, "http://www.w3.org/2001/XMLSchema#integer");
	}
}
