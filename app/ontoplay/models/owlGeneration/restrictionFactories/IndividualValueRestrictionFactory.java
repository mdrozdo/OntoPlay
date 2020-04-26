package ontoplay.models.owlGeneration.restrictionFactories;

import ontoplay.models.ConfigurationException;
import ontoplay.models.angular.update.Annotation;
import ontoplay.models.owlGeneration.RestrictionFactory;
import ontoplay.models.propertyConditions.IndividualValueCondition;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class IndividualValueRestrictionFactory implements RestrictionFactory<IndividualValueCondition> {
    private final OWLDataFactory factory;

    @Inject
    public IndividualValueRestrictionFactory(OWLDataFactory factory) {
        this.factory = factory;
    }

    @Override
    public OWLClassExpression createRestriction(IndividualValueCondition condition) {
        OWLObjectProperty conditionProperty = factory.getOWLObjectProperty(IRI.create(condition.getPropertyUri()));
        OWLNamedIndividual value = factory.getOWLNamedIndividual(IRI.create(condition.getIndividualValue()));
        OWLObjectHasValue restriction = factory.getOWLObjectHasValue(conditionProperty, value);
        return restriction;
    }

    @Override
    public List<OWLAxiom> createIndividualValue(IndividualValueCondition condition, OWLIndividual individual) throws ConfigurationException {
        OWLObjectProperty conditionProperty = factory.getOWLObjectProperty(IRI.create(condition.getPropertyUri()));
        OWLIndividual valueIndividual = factory.getOWLNamedIndividual(IRI.create(condition.getIndividualValue()));
        ArrayList<OWLAxiom> result = new ArrayList<OWLAxiom>();

        OWLObjectPropertyAssertionAxiom assertion = factory.getOWLObjectPropertyAssertionAxiom(conditionProperty, individual, valueIndividual);
        result.add(assertion);

        List<Annotation> tempAnnotations = condition.getAnnotations();
        Set<OWLAnnotation> annotations = new HashSet<>();
        for (Annotation condtionAnnotation : tempAnnotations) {
            OWLAnnotationProperty owlAnnotationProperty = factory
                    .getOWLAnnotationProperty(IRI.create(condtionAnnotation.getUri()));

            annotations.add(factory.getOWLAnnotation(owlAnnotationProperty,
                    factory.getOWLLiteral(condtionAnnotation.getValue())));
        }

        if (annotations.size() != 0) {
            OWLAxiom ax = factory.getOWLObjectPropertyAssertionAxiom(conditionProperty, individual,
                    valueIndividual, annotations);
            result.add(ax);
        }

        return result;
    }


}
