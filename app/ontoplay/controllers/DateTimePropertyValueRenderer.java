package ontoplay.controllers;

import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;

import java.util.HashMap;

public class DateTimePropertyValueRenderer implements
        PropertyValueRenderer {

    @Override
    public void renderOperator(int conditionId, OntoClass owlClass,
                               OntoProperty property, String operator, Renderer renderer) {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("conditionId", conditionId);
        renderer.renderTemplate("Constraints.dateTimeValue", args);
    }

}
