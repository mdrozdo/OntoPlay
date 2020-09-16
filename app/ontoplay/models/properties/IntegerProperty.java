package ontoplay.models.properties;

import ontoplay.models.Constants;
import ontoplay.models.ontologyModel.OwlElement;

public class IntegerProperty extends OwlDatatypeProperty {

    public IntegerProperty(String namespace, String localName, String datatype, String label, OwlElement... domainClasses) {
        super(namespace, localName, datatype, label, domainClasses);
    }

    public IntegerProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        this(namespace, localName, Constants.INTEGER_MAIN_RANGE, label, domainClasses);
    }
}
