package models.properties;

public class DateTimeProperty extends OwlDatatypeProperty {

	public DateTimeProperty(String namespace, String localName,String label) {
		super(namespace, localName, "http://www.w3.org/2001/XMLSchema#dateTime",label);
	}
}
