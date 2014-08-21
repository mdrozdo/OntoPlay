package models.properties;

public class StringProperty extends OwlDatatypeProperty{

	public StringProperty(String namespace, String localName) {
		this(namespace, localName, "http://www.w3.org/2001/XMLSchema#string");
	}
	

	public StringProperty(String namespace, String localName, String datatype) {
		super(namespace, localName, datatype);
		
	}

	
}
