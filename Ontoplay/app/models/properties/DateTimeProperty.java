package models.properties;

public class DateTimeProperty extends OwlDatatypeProperty {

	public DateTimeProperty(String namespace, String localName) {
		super(namespace, localName, "http://www.w3.org/2001/XMLSchema#datetime");
	}
}
