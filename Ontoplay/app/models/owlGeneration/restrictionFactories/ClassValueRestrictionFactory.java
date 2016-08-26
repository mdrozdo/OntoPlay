package models.owlGeneration.restrictionFactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ConfigurationException;
import models.ontologyModel.XsdType;
import models.owlGeneration.IndividualGenerator;
import models.owlGeneration.OntologyGenerator;
import models.owlGeneration.RestrictionFactory;
import models.owlGeneration.ClassRestrictionGenerator;
import models.propertyConditions.ClassValueCondition;
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
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;


public class ClassValueRestrictionFactory extends RestrictionFactory<ClassValueCondition> {
	private final OWLDataFactory factory;
	private final ClassRestrictionGenerator classRestrictionGenerator;
	private final IndividualGenerator individualGenerator;
	
	//TODO: Extract some small interface from OWLApiKB just for generating class restrictions
	public ClassValueRestrictionFactory(ClassRestrictionGenerator classRestrictionGenerator, IndividualGenerator individualGenerator, 
			OWLDataFactory factory) {
		this.classRestrictionGenerator = classRestrictionGenerator;
		this.individualGenerator = individualGenerator;
		this.factory = factory;
	}
	
	@Override
	public OWLClassExpression createRestriction(ClassValueCondition condition) throws ConfigurationException {
		OWLObjectProperty conditionProperty = factory.getOWLObjectProperty(IRI.create(condition.getPropertyUri()));

		OWLClassExpression nestedCondition = classRestrictionGenerator.convertToOntClass(null, condition.getClassConstraintValue());
		OWLObjectSomeValuesFrom propertyRestriction = factory.getOWLObjectSomeValuesFrom(conditionProperty, nestedCondition);
		return propertyRestriction;
	}

	@Override
	public List<OWLAxiom> createIndividualValue(ClassValueCondition condition, OWLIndividual individual) throws ConfigurationException {
		OWLObjectProperty conditionProperty = factory.getOWLObjectProperty(IRI.create(condition.getPropertyUri()));
		OWLIndividual nestedIndividual = factory.getOWLAnonymousIndividual();

		List<OWLAxiom> nestedAxioms = individualGenerator.createPropertyAxioms(nestedIndividual, condition.getClassConstraintValue());
		OWLObjectPropertyAssertionAxiom assertion = factory.getOWLObjectPropertyAssertionAxiom(conditionProperty, individual, nestedIndividual);
		ArrayList<OWLAxiom> result = new ArrayList<OWLAxiom>();
		result.addAll(nestedAxioms);
		result.add(assertion);
		
		return result;
	}
	

}
