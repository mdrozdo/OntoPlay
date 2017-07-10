package ontoplay.models.angular;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the structure of the xml file
 *
 * @author Motasem Alwazir
 */

public class AnnotationCFDTO {
    private String annotationId;
    private String annotationIri;
    private String annotationName;
    private List<ComponentCF> components;

    public AnnotationCFDTO() {
        this.annotationId = "";
        this.annotationIri = "";
        this.annotationName = "";
        components = new ArrayList<ComponentCF>();
    }


    public AnnotationCFDTO(String annotationId, String annotationIri, String annotationName) {
        if (annotationId == null)
            annotationId = "";
        if (annotationIri == null)
            annotationIri = "";
        if (annotationName == null)
            annotationName = "";
        this.annotationId = annotationId;
        this.annotationIri = annotationIri;
        this.annotationName = annotationName;
        components = new ArrayList<ComponentCF>();
    }

    /**
     * @return the annotationId
     */
    public String getAnnotationId() {
        return annotationId;
    }

    /**
     * @param annotationId the annotationId to set
     */
    public void setAnnotationId(String annotationId) {
        this.annotationId = annotationId;
    }

    /**
     * @return the annotationIri
     */
    public String getAnnotationIri() {
        return annotationIri;
    }

    /**
     * @param annotationIri the annotationIri to set
     */
    public void setAnnotationIri(String annotationIri) {
        this.annotationIri = annotationIri;
    }

    /**
     * @return the annotationName
     */
    public String getAnnotationName() {
        return annotationName;
    }

    /**
     * @param annotationName the annotationName to set
     */
    public void setAnnotationName(String annotationName) {
        this.annotationName = annotationName;
    }

    /**
     * @return the components
     */
    public List<ComponentCF> getComponents() {
        return components;
    }

    /**
     * @param components the components to set
     */
    public void setComponents(List<ComponentCF> components) {
        this.components = components;
    }


    public void addComponent(ComponentCF componentCF) {
        this.components.add(componentCF);

    }


}