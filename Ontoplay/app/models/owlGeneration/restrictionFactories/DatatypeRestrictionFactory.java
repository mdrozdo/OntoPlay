package models.owlGeneration.restrictionFactories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ConfigurationException;
import models.ontologyModel.OntoProperty;
import models.owlGeneration.RestrictionFactory;
import models.propertyConditions.DatatypePropertyCondition;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;




public class DatatypeRestrictionFactory extends RestrictionFactory<DatatypePropertyCondition>{
	private Map<Class, Map<String, DatatypeRestrictionFactory>> factoryRegistry = new HashMap<Class, Map<String, DatatypeRestrictionFactory>>();	
	
	@Override
	public OWLClassExpression createRestriction(DatatypePropertyCondition condition) throws ConfigurationException {
		DatatypeRestrictionFactory factory = getOperatorRestrictionFactory(condition);
		return factory.createRestriction(condition);
	}

	private DatatypeRestrictionFactory getOperatorRestrictionFactory(DatatypePropertyCondition condition) throws ConfigurationException {
		if(condition.getProperty()== null){
			throw new RuntimeException(String.format("The condition for uri: %s has not been initialized with a property object", condition.getPropertyUri()));
		}		
		
		
		Class<? extends OntoProperty> propertyClass = condition.getProperty().getClass();
		if(!factoryRegistry.containsKey(propertyClass)){
			throw new ConfigurationException(String.format("The restriction factory for the %s property class has not been found", propertyClass.getName()));
		}
		if(!factoryRegistry.get(propertyClass).containsKey(condition.getOperator())){
			throw new ConfigurationException(String.format("The restriction factory for the %s property class and %s operator has not been found", propertyClass.getName(), condition.getOperator()));
		}
		
		return factoryRegistry.get(propertyClass).get(condition.getOperator());
	}

	public void registerOperatorRestrictionFactory(Class propertyType, String operator, DatatypeRestrictionFactory factory) {
		if(!factoryRegistry.containsKey(propertyType)){
			factoryRegistry.put(propertyType, new HashMap<String, DatatypeRestrictionFactory>());
		} 
		factoryRegistry.get(propertyType).put(operator, factory);
	}

	@Override
	public List<OWLAxiom> createIndividualValue(DatatypePropertyCondition condition, OWLIndividual individual) throws ConfigurationException {
		DatatypeRestrictionFactory factory = getOperatorRestrictionFactory(condition);
		return factory.createIndividualValue(condition, individual);
	}
}
