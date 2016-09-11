package ontoplay.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ontoplay.models.PropertyOperator;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import play.Logger.ALogger;

public class DatatypePropertyRenderer extends PropertyConditionRenderer {
	
	private Map<String, PropertyValueRenderer> valueRenderers = new HashMap<String, PropertyValueRenderer>();
	private List<PropertyOperator> operators = new ArrayList<PropertyOperator>();
	
	@Override
	public void renderProperty(int conditionId, OntoClass owlClass, OntoProperty prop, boolean isDescriptionOfIndividual, Renderer renderer) {
		Map<String, Object> args = new LinkedHashMap<String, Object>();
		args.put("conditionId", conditionId);
		args.put("classUri", owlClass.getUri());
		args.put("propertyUri", prop.getUri());
		if(!isDescriptionOfIndividual)
			args.put("operators", operators);
		else
			args.put("operators", getOperatorsForIndividual());
		
		renderer.renderTemplate("Constraints.datatypeCondition", args);
	}

	private List<PropertyOperator> getOperatorsForIndividual() {
		ALogger log = play.Logger.of("application");
		List<PropertyOperator> filteredOperators = new ArrayList<PropertyOperator>();
		for (PropertyOperator propertyOperator : operators) {
			log.info(propertyOperator.getName());
			if(propertyOperator.canDescribeIndividual()){				
				filteredOperators.add(propertyOperator);
				
			}
		}
		return filteredOperators;
	}

	@Override
	public void renderOperator(int conditionId, OntoClass owlClass,
			OntoProperty property, String operator, Renderer renderer) {
		valueRenderers.get(operator).renderOperator(conditionId, owlClass, property, operator, renderer);		
	}

	public void registerPropertyOperator(
			String operatorName,
			String operatorDescription,
			boolean canDescribeIndividual,
			PropertyValueRenderer propertyValueRenderer) {
		valueRenderers.put(operatorName, propertyValueRenderer);
		operators.add(new PropertyOperator(operatorName, operatorDescription, canDescribeIndividual));
		
	}
	
	public void registerPropertyOperator(
			String operatorName,
			String operatorDescription,
			PropertyValueRenderer propertyValueRenderer) {
		registerPropertyOperator(operatorName, operatorDescription, false, propertyValueRenderer);
	}

	@Override
	public List<PropertyOperator> getOperators(boolean isDescriptionOfIndividual) {
		if(!isDescriptionOfIndividual)
			return operators;
		else
			return getOperatorsForIndividual();
	}



}
