package models.properties;

public class IntegerProperty extends OwlDatatypeProperty {
	
	public IntegerProperty(String namespace, String localName, String datatype,String label) {
		super(namespace, localName, datatype,label);
	}

	public IntegerProperty(String namespace, String localName,String label) {
		this(namespace, localName, "http://www.w3.org/2001/XMLSchema#integer",label);
	}
}
