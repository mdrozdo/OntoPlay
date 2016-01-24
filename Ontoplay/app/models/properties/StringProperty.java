package models.properties;

public class StringProperty extends OwlDatatypeProperty{

	public StringProperty(String namespace, String localName,String label) {
		this(namespace, localName, "http://www.w3.org/2001/XMLSchema#string",label);
	}
	

	public StringProperty(String namespace, String localName, String datatype,String label) {
		super(namespace, localName, datatype,label);
		
	}

	
}
