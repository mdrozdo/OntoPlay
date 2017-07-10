package ontoplay.models.propertyConditions;

import ontoplay.models.PropertyValueCondition;

public class IndividualValueCondition extends PropertyValueCondition {
    private String individualValue;

    public IndividualValueCondition() {
    }

    public IndividualValueCondition(String propertyUri, String individualValue) {
        super(propertyUri);
        this.individualValue = individualValue;
    }

    public String getIndividualValue() {
        return individualValue;
    }

    public void setIndividualValue(String individualValue) {
        this.individualValue = individualValue;
    }
}
