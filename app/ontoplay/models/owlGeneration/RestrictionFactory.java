package ontoplay.models.owlGeneration;

import java.util.HashMap;
import java.util.List;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;

import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyValueCondition;



public abstract class RestrictionFactory<T extends PropertyValueCondition> {

	public abstract OWLClassExpression createRestriction(T condition) throws ConfigurationException;
	public abstract List<OWLAxiom> createIndividualValue(T condition, OWLIndividual individual) throws ConfigurationException;
}
