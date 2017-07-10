package ontoplay.models.angular.update;

import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent annotation axiom
 *
 * @author Motasem Alwazir
 */

public class AnnotationAxiom {
    private String property;
    private String source;
    private RDFNode target;
    private List<String> annotationProperty;
    private List<String> annotationPropertyValue;


    public AnnotationAxiom() {
        annotationProperty = new ArrayList<String>();
        annotationPropertyValue = new ArrayList<String>();
    }


    public AnnotationAxiom(String property, String source, RDFNode target) {
        this.property = property;
        this.source = source;
        this.target = target;
    }

    public static List<AnnotationAxiom> readIterator(ResIterator annotationAxiomsIterator) {
        AnnotationAxiom temmAnnotationAxiom = null;
        List<AnnotationAxiom> annotationAxioms = new ArrayList<AnnotationAxiom>();
        while (annotationAxiomsIterator.hasNext()) {
            Resource axiom = annotationAxiomsIterator.next();
            StmtIterator stmts = axiom.listProperties();
            temmAnnotationAxiom = new AnnotationAxiom();
            while (stmts.hasNext()) {
                Statement stmt = stmts.next();
                temmAnnotationAxiom.setField(stmt.getPredicate().getURI(), stmt.getObject());
            }
            annotationAxioms.add(temmAnnotationAxiom);
        }
        return annotationAxioms;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public RDFNode getTarget() {
        return target;
    }

    public void setTarget(RDFNode target) {
        this.target = target;
    }

    public List<String> getAnnotationProperty() {
        return annotationProperty;
    }

    public void setAnnotationProperty(List<String> annotationProperty) {
        this.annotationProperty = annotationProperty;
    }

    public List<String> getAnnotationPropertyValue() {
        return annotationPropertyValue;
    }

    public void setAnnotationPropertyValue(List<String> annotationPropertyValue) {
        this.annotationPropertyValue = annotationPropertyValue;
    }

    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("property: ").append(property).append("\n")
                .append("source: ").append(source).append("\n")
                .append("target: ").append(target).append("\n")
                .append("annotationProperty: ").append(annotationProperty).append("\n")
                .append("annotationPropertyValue: ").append(annotationPropertyValue).append("\n");
        return resultBuilder.toString();

    }

    public void setField(String predict, RDFNode rdfNode) {
        if (predict.equals("http://www.w3.org/2002/07/owl#annotatedProperty"))
            setProperty(rdfNode.toString());
        else if (predict.equals("http://www.w3.org/2002/07/owl#annotatedSource"))
            setSource(rdfNode.toString());
        else if (predict.equals("http://www.w3.org/2002/07/owl#annotatedTarget")) {
            if (rdfNode.isResource()) {
                setTarget(rdfNode.as(Individual.class));
            } else {
                setTarget(rdfNode);
            }
        } else if (predict.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {

        } else {
            try {
                addAnnotationPropertyValue(rdfNode.toString().substring(0, rdfNode.toString().lastIndexOf("^^")));
                addAnnotationProperty(predict);
            } catch (Exception e) {
                System.out.println(" Error reading predict of Axiom in AnnotationAxiom" + e.toString());
            }

        }
    }

    private void addAnnotationProperty(String predict) {
        annotationProperty.add(predict);

    }

    private void addAnnotationPropertyValue(String substring) {
        annotationPropertyValue.add(substring);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        AnnotationAxiom other = (AnnotationAxiom) obj;
        if (!other.getTarget().isAnon())
            return this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget())
                    && this.property.equals(other.getProperty());
        else {
            return false;
        }

    }
}