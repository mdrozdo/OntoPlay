package ontoplay.controllers;

import ontoplay.models.PropertyOperator;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.properties.OwlObjectProperty;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObjectPropertyRenderer implements PropertyConditionRenderer<OwlObjectProperty> {

    private OntologyReader ontoReader;

    @Inject
    public ObjectPropertyRenderer(OntologyReader ontoReader) {

        this.ontoReader = ontoReader;
    }

    @Override
    public void renderProperty(int conditionId, OntoClass owlClass, OwlObjectProperty prop, boolean isDescriptionOfIndividual,
                               Renderer renderer) {
        Map<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("conditionId", conditionId);
        args.put("classUri", owlClass.getUri());
        args.put("propertyUri", prop.getUri());
        args.put("isDescriptionOfIndividual", isDescriptionOfIndividual);

        renderer.renderTemplate("Constraints.objectCondition", args);
    }

    @Override
    public void renderOperator(int conditionId, OntoClass owlClass,
                               OwlObjectProperty property, String operator, Renderer renderer) {
        if (operator.equals("equalToIndividual")) {
            renderIndividualValueCondition(conditionId, owlClass, property, operator, renderer);
        } else if (operator.equals("constrainedBy")) {
            renderConstrainedValueCondition(conditionId, owlClass, property, operator, false, renderer);
        } else if (operator.equals("describedWith")) {
            renderConstrainedValueCondition(conditionId, owlClass, property, operator, true, renderer);
        }

    }

    private void renderConstrainedValueCondition(int conditionId,
                                                 OntoClass owlClass, OntoProperty property, String operator,
                                                 boolean isDescriptionOfIndividual, Renderer renderer) {

        Map<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("conditionId", conditionId);
        args.put("classes", ontoReader.getClassesInRange(property));
        args.put("isDescriptionOfIndividual", isDescriptionOfIndividual);

        renderer.renderTemplate("Constraints.constrainedValueCondition", args);
    }

    private void renderIndividualValueCondition(int conditionId,
                                                OntoClass owlClass, OntoProperty property, String operator,
                                                Renderer renderer) {

        Map<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("individuals", ontoReader.getIndividualsInRange(owlClass, property));

        renderer.renderTemplate("Constraints.individualValueCondition", args);

    }

    @Override
    public List<PropertyOperator> getOperators(boolean isDescriptionOfIndividual) {
        List<PropertyOperator> filteredOperators = new ArrayList<PropertyOperator>();
        filteredOperators.add(new PropertyOperator("equalToIndividual", "is equal to individual", true));

        if (isDescriptionOfIndividual) {
            filteredOperators.add(new PropertyOperator("describedWith", "is described with", true));
        } else {
            filteredOperators.add(new PropertyOperator("constrainedBy", " is constrained by", false));
        }
        return filteredOperators;
    }
}
