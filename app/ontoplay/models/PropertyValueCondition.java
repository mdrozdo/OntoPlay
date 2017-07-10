package ontoplay.models;

import ontoplay.models.angular.update.Annotation;
import ontoplay.models.ontologyModel.OntoProperty;

import java.util.ArrayList;
import java.util.List;


public class PropertyValueCondition<T extends OntoProperty> {
    private String propertyUri;
    private T property;
    private List<Annotation> annotations = new ArrayList<Annotation>();

    public PropertyValueCondition() {

    }

    public PropertyValueCondition(String propertyUri) {
        this.propertyUri = propertyUri;
    }

    public String getPropertyUri() {
        return propertyUri;
    }

    public void setPropertyUri(String propertyUri) {
        this.propertyUri = propertyUri;
    }

    public T getProperty() {
        return property;
    }

    public void setProperty(T property) {
        this.property = property;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }


}
