package ontoplay.models;

import java.util.List;

/**
 * Created by drozd on 17.12.2018.
 */
public class PropertyGroupCondition implements PropertyCondition {
    private List<PropertyCondition> contents;
    private PropertyConditionType type;

    public PropertyGroupCondition(){
    }

    public List<PropertyCondition> getContents() {
        return contents;
    }

    public void setContents(List<PropertyCondition> contents) {
        this.contents = contents;
    }

    public PropertyConditionType getType() {
        return type;
    }

    public void setType(PropertyConditionType type) {
        this.type = type;
    }
}



