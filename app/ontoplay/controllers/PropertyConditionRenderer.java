package ontoplay.controllers;

import java.util.HashMap;
import java.util.List;

import ontoplay.models.PropertyOperator;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;

public abstract class PropertyConditionRenderer {

	private static HashMap<Class, PropertyConditionRenderer> renderers = new HashMap<Class, PropertyConditionRenderer>();

	public static void registerPropertyTypeRenderer(Class type,
			PropertyConditionRenderer renderer) {
			renderers.put(type, renderer);
	}

	public static PropertyConditionRenderer getRenderer(Class<?> type) {
		PropertyConditionRenderer propertyConditionRenderer = renderers.get(type);
		return propertyConditionRenderer;
	}

	public abstract void renderProperty(int conditionId, OntoClass owlClass,
			OntoProperty prop, boolean isDescriptionOfIndividual, Renderer renderer);

	public abstract void renderOperator(int conditionId, OntoClass owlClass,
			OntoProperty property, String operator, Renderer renderer);
	
	public abstract  List<PropertyOperator> getOperators(boolean isDescriptionOfIndividual);

}
