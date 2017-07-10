package ontoplay.controllers;

import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;

import java.util.HashMap;

public class SimpleDatatypePropertyValueRenderer implements
        PropertyValueRenderer {

    @Override
    public void renderOperator(int conditionId, OntoClass owlClass,
                               OntoProperty property, String operator, Renderer renderer) {
        renderer.renderTemplate("Constraints.simpleDatatypeValue", new HashMap<String, Object>());
    }

}
