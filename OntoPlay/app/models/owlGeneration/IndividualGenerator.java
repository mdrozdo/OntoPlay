package models.owlGeneration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.ClassCondition;
import models.ConfigurationException;
import models.PropertyValueCondition;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;

public class IndividualGenerator{
	
	private final OWLDataFactory factory;

	public IndividualGenerator(OWLDataFactory factory){
		this.factory = factory;
	}
	
	public List<OWLAxiom> convertToOntIndividual(String individualUri, ClassCondition condition) throws ConfigurationException {
		OWLIndividual individual = factory.getOWLNamedIndividual(IRI.create(individualUri));
		
		return createPropertyAxioms(individual, condition);
	}

	public List<OWLAxiom> createPropertyAxioms(OWLIndividual individual, ClassCondition condition) throws ConfigurationException {
		OWLClass conditionClass = factory.getOWLClass(IRI.create(condition.getClassUri()));
		
		OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(conditionClass, individual);
				
		ArrayList<OWLAxiom> individualAxioms = new ArrayList<OWLAxiom>();
		individualAxioms.add(classAssertionAxiom);
		
		for (PropertyValueCondition cond : condition.getPropertyConditions()) {
			RestrictionFactory restrictionFactory = RestrictionFactory.getRestrictionFactory(cond);
			List<OWLAxiom> propertyAxioms = restrictionFactory.createIndividualValue(cond, individual);
			individualAxioms.addAll(propertyAxioms);
		}
		return individualAxioms;
	}
}