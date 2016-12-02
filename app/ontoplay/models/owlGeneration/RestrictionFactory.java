package ontoplay.models.owlGeneration;

import java.util.HashMap;
import java.util.List;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;

import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyValueCondition;



public interface RestrictionFactory<T extends PropertyValueCondition> {

	OWLClassExpression createRestriction(T condition) throws ConfigurationException;
	List<OWLAxiom> createIndividualValue(T condition, OWLIndividual individual) throws ConfigurationException;
}
