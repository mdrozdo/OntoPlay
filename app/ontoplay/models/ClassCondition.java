package ontoplay.models;

import ontoplay.models.angular.update.Annotation;

import java.util.ArrayList;
import java.util.List;

public class ClassCondition {
    private String classUri;
    private PropertyCondition propertyConditions;
    private List<Annotation> annotations = new ArrayList<Annotation>();
    private ClassRelation classRelation = ClassRelation.EQUIVALENT;

    public ClassCondition() {

    }

    public ClassCondition(String classUri) {
        setClassUri(classUri);
    }

    public ClassRelation getClassRelation() {
        return classRelation;
    }

    public void setClassRelation(ClassRelation classRelation) {
        this.classRelation = classRelation;
    }

    public String getClassUri() {
        return classUri;
    }

    public void setClassUri(String classUri) {
        this.classUri = classUri;
    }

    public PropertyCondition getPropertyConditions() {
        return propertyConditions;
    }

    public void setPropertyConditions(PropertyCondition propertyConditions) {
        this.propertyConditions = propertyConditions;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
