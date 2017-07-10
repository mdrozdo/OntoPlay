package ontoplay.models.owlGeneration;

import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyValueCondition;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;

import java.util.List;


public interface RestrictionFactory<T extends PropertyValueCondition> {

    OWLClassExpression createRestriction(T condition) throws ConfigurationException;

    List<OWLAxiom> createIndividualValue(T condition, OWLIndividual individual) throws ConfigurationException;
}
