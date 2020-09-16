package ontoplay.models.properties;

import ontoplay.models.Constants;
import ontoplay.models.ontologyModel.OwlElement;

public class FloatProperty extends OwlDatatypeProperty {

    public FloatProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        super(namespace, localName, Constants.FLOAT_MAIN_RANGE, label, domainClasses);
    }

    public FloatProperty(String namespace, String localName, String datatypeUri, String label, OwlElement... domainClasses) {
        super(namespace, localName, datatypeUri, label, domainClasses);
    }
}
