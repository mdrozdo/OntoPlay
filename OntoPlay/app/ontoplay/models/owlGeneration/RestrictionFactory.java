package ontoplay.models.owlGeneration;

import java.util.HashMap;
import java.util.List;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;

import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyValueCondition;



public abstract class RestrictionFactory<T extends PropertyValueCondition> {
	private static HashMap<Class, Object> factories = new HashMap<Class, Object>();
	
	public static <U extends PropertyValueCondition> void registerRestrictionTypeFactory(Class<U> type, RestrictionFactory<U> factory) {
		factories.put(type, factory);		
	}

	public static <U extends PropertyValueCondition> RestrictionFactory<U> getRestrictionFactory(U condition) throws ConfigurationException {
		if(!factories.containsKey(condition.getClass())){
			throw new ConfigurationException(String.format("RestrictionFactory for class %s has not been configured.", condition.getClass().getName()));
		}
		return (RestrictionFactory<U>)  factories.get(condition.getClass());
	}
	
	public abstract OWLClassExpression createRestriction(T condition) throws ConfigurationException;
	public abstract List<OWLAxiom> createIndividualValue(T condition, OWLIndividual individual) throws ConfigurationException;
}
