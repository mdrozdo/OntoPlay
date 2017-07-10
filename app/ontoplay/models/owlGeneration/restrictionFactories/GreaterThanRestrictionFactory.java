package ontoplay.models.owlGeneration.restrictionFactories;

import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLFacet;


//TODO: Unnecessary inheritance from DatatypeRestrictionFactory. Could be a new interface or something
public class GreaterThanRestrictionFactory extends DatatypeRestrictionFactory {
    private final String dataTypeUri;
    private final OWLDataFactory factory;


    public GreaterThanRestrictionFactory(String dataTypeUri, OWLDataFactory factory) {
        this.dataTypeUri = dataTypeUri;
        this.factory = factory;
    }

    @Override
    public OWLClassExpression createRestriction(DatatypePropertyCondition condition) {
        OWLDataProperty conditionProperty = factory.getOWLDataProperty(IRI.create(condition.getPropertyUri()));
        OWLDatatype xsdType = factory.getOWLDatatype(IRI.create(dataTypeUri));
        OWLLiteral value = factory.getOWLLiteral(condition.getDatatypeValue(), xsdType);
        OWLDatatypeRestriction dataRange = factory.getOWLDatatypeRestriction(xsdType, OWLFacet.MIN_EXCLUSIVE, value);
        OWLDataSomeValuesFrom restriction = factory.getOWLDataSomeValuesFrom(conditionProperty, dataRange);
        return restriction;
    }

}
