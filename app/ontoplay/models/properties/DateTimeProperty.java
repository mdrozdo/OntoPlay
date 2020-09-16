package ontoplay.models.properties;

import ontoplay.models.Constants;
import ontoplay.models.ontologyModel.OwlElement;

public class DateTimeProperty extends OwlDatatypeProperty {

    public DateTimeProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        super(namespace, localName, Constants.DATE_MAIN_RANGE, label, domainClasses);
    }
}
