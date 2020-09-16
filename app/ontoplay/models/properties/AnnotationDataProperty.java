package ontoplay.models.properties;

import ontoplay.models.ontologyModel.OwlElement;

public class AnnotationDataProperty extends OwlDatatypeProperty {

    public AnnotationDataProperty(String namespace, String localName, String label, OwlElement... domainClasses) {
        this(namespace, localName, "http://www.w3.org/2001/XMLSchema#string", label, domainClasses);
    }


    public AnnotationDataProperty(String namespace, String localName, String datatype, String label, OwlElement... domainClasses) {
        super(namespace, localName, datatype, label, domainClasses);

    }


}
