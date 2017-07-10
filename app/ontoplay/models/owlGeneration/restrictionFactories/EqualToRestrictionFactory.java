package ontoplay.models.owlGeneration.restrictionFactories;

import ontoplay.models.ConfigurationException;
import ontoplay.models.angular.update.Annotation;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: Unnecessary inheritance from DatatypeRestrictionFactory. Could be a new interface or something
public class EqualToRestrictionFactory extends DatatypeRestrictionFactory {
    private final String dataTypeUri;
    private final OWLDataFactory factory;

    public EqualToRestrictionFactory(String dataTypeUri, OWLDataFactory factory) {
        this.dataTypeUri = dataTypeUri;
        this.factory = factory;
    }

    @Override
    public OWLClassExpression createRestriction(DatatypePropertyCondition condition) {
        OWLDataProperty conditionProperty = factory.getOWLDataProperty(IRI.create(condition.getPropertyUri()));
        OWLLiteral value = getValueLiteral(condition);
        OWLDataHasValue restriction = factory.getOWLDataHasValue(conditionProperty, value);
        return restriction;
    }

    private OWLLiteral getValueLiteral(DatatypePropertyCondition condition) {
        OWLDatatype owlDatatype = factory.getOWLDatatype(IRI.create(condition.getProperty().getDatatype()));
        OWLLiteral value = factory.getOWLLiteral(condition.getDatatypeValue(), owlDatatype);
        return value;
    }

    @Override
    public List<OWLAxiom> createIndividualValue(DatatypePropertyCondition condition, OWLIndividual individual)
            throws ConfigurationException {
        OWLDataProperty conditionProperty = factory.getOWLDataProperty(IRI.create(condition.getPropertyUri()));
        OWLLiteral value = getValueLiteral(condition);
        OWLDataPropertyAssertionAxiom assertion = factory.getOWLDataPropertyAssertionAxiom(conditionProperty,
                individual, value);

        OWLAnnotationProperty owlAnnotationProperty2 = factory
                .getOWLAnnotationProperty(IRI.create(condition.getPropertyUri()));

        ArrayList<OWLAxiom> result = new ArrayList<OWLAxiom>();
        result.add(assertion);

        Set<OWLAnnotation> annotations = new HashSet<>();

        for (Annotation condtionAnnotation : condition.getAnnotations()) {
            OWLAnnotationProperty owlAnnotationProperty = factory
                    .getOWLAnnotationProperty(IRI.create(condtionAnnotation.getUri()));

            annotations.add(factory.getOWLAnnotation(owlAnnotationProperty,
                    factory.getOWLLiteral(condtionAnnotation.getValue())));
        }
        if (annotations.size() != 0) {
            OWLAxiom ax;
            if (!individual.isAnonymous())
                ax = factory.getOWLDataPropertyAssertionAxiom(conditionProperty, individual,
                        factory.getOWLLiteral(condition.getDatatypeValue()), annotations);
            else {
                ax = factory.getOWLDataPropertyAssertionAxiom(conditionProperty, individual.asOWLAnonymousIndividual(),
                        factory.getOWLLiteral(condition.getDatatypeValue()), annotations);
            }
            result.add(ax);
        }

        return result;
    }
}
