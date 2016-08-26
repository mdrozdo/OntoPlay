package models.owlGeneration.restrictionFactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ConfigurationException;
import models.ontologyModel.XsdType;
import models.owlGeneration.RestrictionFactory;
import models.propertyConditions.DatatypePropertyCondition;
import models.propertyConditions.IndividualValueCondition;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;


public class IndividualValueRestrictionFactory extends RestrictionFactory<IndividualValueCondition> {
	private final OWLDataFactory factory;
	
	public IndividualValueRestrictionFactory(OWLDataFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public OWLClassExpression createRestriction(IndividualValueCondition condition) {
		OWLObjectProperty conditionProperty = factory.getOWLObjectProperty(IRI.create(condition.getPropertyUri()));
		OWLNamedIndividual value = factory.getOWLNamedIndividual(IRI.create(condition.getIndividualValue()));
		OWLObjectHasValue restriction = factory.getOWLObjectHasValue(conditionProperty, value);
		return restriction;
	}

	@Override
	public List<OWLAxiom> createIndividualValue(IndividualValueCondition condition, OWLIndividual individual) throws ConfigurationException {
		OWLObjectProperty conditionProperty = factory.getOWLObjectProperty(IRI.create(condition.getPropertyUri()));
		OWLIndividual valueIndividual = factory.getOWLNamedIndividual(IRI.create(condition.getIndividualValue()));

		OWLObjectPropertyAssertionAxiom assertion = factory.getOWLObjectPropertyAssertionAxiom(conditionProperty, individual, valueIndividual);
		ArrayList<OWLAxiom> result = new ArrayList<OWLAxiom>();
		result.add(assertion);
		
		return result;
	}
	

}
