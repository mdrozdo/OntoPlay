package models.owlGeneration.restrictionFactories;

import models.propertyConditions.DatatypePropertyCondition;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;
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
