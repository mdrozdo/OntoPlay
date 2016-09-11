package ontoplay.models.properties;

import ontoplay.models.Constants;

public class DateTimeProperty extends OwlDatatypeProperty {

	public DateTimeProperty(String namespace, String localName,String label) {
		super(namespace, localName, Constants.DATE_MAIN_RANGE,label);
	}
}
