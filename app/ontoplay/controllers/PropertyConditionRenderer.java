package ontoplay.controllers;

import ontoplay.models.PropertyOperator;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;

import java.util.List;

public interface PropertyConditionRenderer<T extends OntoProperty> {
    void renderProperty(int conditionId, OntoClass owlClass,
                        T prop, boolean isDescriptionOfIndividual, Renderer renderer);

    void renderOperator(int conditionId, OntoClass owlClass,
                        T property, String operator, Renderer renderer);

    List<PropertyOperator> getOperators(boolean isDescriptionOfIndividual);

}
