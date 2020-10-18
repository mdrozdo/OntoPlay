package ontoplay.models.properties;

import ontoplay.models.Constants;
import ontoplay.models.ontologyModel.OwlElement;

public class TimeProperty extends OwlDatatypeProperty {

    public TimeProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        super(namespace, localName, Constants.TIME_MAIN_RANGE, label, domainClasses);
    }
}
