package models.owlGeneration.restrictionFactories;

import java.util.ArrayList;
import java.util.List;
import models.ConfigurationException;
import models.propertyConditions.DatatypePropertyCondition;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;


//TODO: Unnecessary inheritance from DatatypeRestrictionFactory. Could be a new interface or something
public class EqualToRestrictionFactory extends DatatypeRestrictionFactory {
	private final String dataTypeUri;
	private final OWLDataFactory factory;
	
	public EqualToRestrictionFactory(String dataTypeUri, OWLDataFactory factory) {
		this.dataTypeUri = dataTypeUri;
		this.factory = factory;
	}
	
	@Override
	public OWLClassExpression createRestriction(DatatypePropertyCondition condition) {
		OWLDataProperty conditionProperty = factory.getOWLDataProperty(IRI.create(condition.getPropertyUri()));
		OWLLiteral value = getValueLiteral(condition);
		OWLDataHasValue restriction = factory.getOWLDataHasValue(conditionProperty, value);
		return restriction;
	}
	
	private OWLLiteral getValueLiteral(DatatypePropertyCondition condition){
		OWLDatatype owlDatatype = factory.getOWLDatatype(IRI.create(condition.getProperty().getDatatype()));
		OWLLiteral value = factory.getOWLLiteral(condition.getDatatypeValue(), owlDatatype);
		return value;
	}
	
	@Override
	public List<OWLAxiom> createIndividualValue(DatatypePropertyCondition condition, OWLIndividual individual) throws ConfigurationException {
		OWLDataProperty conditionProperty = factory.getOWLDataProperty(IRI.create(condition.getPropertyUri()));
		OWLLiteral value = getValueLiteral(condition);
		OWLDataPropertyAssertionAxiom assertion = factory.getOWLDataPropertyAssertionAxiom(conditionProperty, individual, value);

		ArrayList<OWLAxiom> result = new ArrayList<OWLAxiom>();
		result.add(assertion);

		return result;
	}
}
