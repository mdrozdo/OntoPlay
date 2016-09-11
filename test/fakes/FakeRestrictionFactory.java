package fakes;

import java.util.List;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;

import ontoplay.models.ConfigurationException;
import ontoplay.models.owlGeneration.RestrictionFactory;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;

public class FakeRestrictionFactory extends RestrictionFactory<DatatypePropertyCondition> {
	
	private DatatypePropertyCondition receivedCondition;

	public DatatypePropertyCondition getReceivedCondition() {
		return receivedCondition;
	}

	@Override
	public OWLClassExpression createRestriction(DatatypePropertyCondition condition) throws ConfigurationException {
		receivedCondition = condition;
		return null;
	}

	@Override
	public List<OWLAxiom> createIndividualValue(DatatypePropertyCondition condition, OWLIndividual individual) throws ConfigurationException {
		receivedCondition = condition;
		return null;
	}

}
