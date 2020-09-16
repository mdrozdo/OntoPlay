package ontoplay.models.properties;

import ontoplay.models.Constants;
import ontoplay.models.ontologyModel.OwlElement;

public class StringProperty extends OwlDatatypeProperty {

    public StringProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        this(namespace, localName, Constants.STRING_MAIN_RANGE, label, domainClasses);
    }


    public StringProperty(String namespace, String localName, String datatype, String label, OwlElement... domainClasses) {
        super(namespace, localName, datatype, label, domainClasses);

    }


}
