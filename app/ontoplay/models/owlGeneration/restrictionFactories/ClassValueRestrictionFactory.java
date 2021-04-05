package ontoplay.models.owlGeneration.restrictionFactories;

import ontoplay.OntoplayConfig;
import ontoplay.models.ConfigurationException;
import ontoplay.models.Constants;
import ontoplay.models.PropertyConditionType;
import ontoplay.models.PropertyGroupCondition;
import ontoplay.models.dto.update.Annotation;
import ontoplay.models.owlGeneration.*;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ClassValueRestrictionFactory implements RestrictionFactory<ClassValueCondition> {
    private final OWLDataFactory factory;
    private final ClassRestrictionGenerator classRestrictionGenerator;
    private final IndividualGenerator individualGenerator;
    private final OntoplayConfig config;

    //TODO: Extract some small interface from OWLApiKB just for generating class restrictions
    @Inject
    public ClassValueRestrictionFactory(ClassRestrictionGeneratorFactory classRestrictionGeneratorFactory, IndividualGeneratorFactory individualGeneratorFactory,
                                        OWLDataFactory factory, OntoplayConfig config) {
        this.classRestrictionGenerator = classRestrictionGeneratorFactory.create(factory);
        this.individualGenerator = individualGeneratorFactory.create(factory);
        this.factory = factory;
        this.config = config;
    }

    @Override
    public OWLClassExpression createRestriction(ClassValueCondition condition) throws ConfigurationException {
        OWLObjectProperty conditionProperty = factory.getOWLObjectProperty(IRI.create(condition.getPropertyUri()));

        OWLClassExpression nestedCondition = classRestrictionGenerator.convertToOntClass(null, condition.getClassConstraintValue());
        OWLObjectSomeValuesFrom propertyRestriction = factory.getOWLObjectSomeValuesFrom(conditionProperty, nestedCondition);
        return propertyRestriction;
    }

    @Override
    public List<OWLAxiom> createIndividualValue(ClassValueCondition condition, OWLIndividual individual) throws ConfigurationException {
        OWLObjectProperty conditionProperty = factory.getOWLObjectProperty(IRI.create(condition.getPropertyUri()));

        String individualUri = extractIndividualUri(condition);

        OWLIndividual nestedIndividual = individualUri == null
                ? factory.getOWLAnonymousIndividual()
                : factory.getOWLNamedIndividual(IRI.create(config.getOntologyNamespace() + individualUri));

        List<OWLAxiom> nestedAxioms = individualGenerator.createPropertyAxioms(nestedIndividual, condition.getClassConstraintValue());
        OWLObjectPropertyAssertionAxiom assertion = factory.getOWLObjectPropertyAssertionAxiom(conditionProperty, individual, nestedIndividual);
        ArrayList<OWLAxiom> result = new ArrayList<OWLAxiom>();
        result.addAll(nestedAxioms);
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
                    nestedIndividual.asOWLAnonymousIndividual(), annotations);
            result.add(ax);
        }
        return result;
    }

    private String extractIndividualUri(ClassValueCondition condition) {
        var innerCondition = condition.getClassConstraintValue().getPropertyConditions();
        if (innerCondition instanceof DatatypePropertyCondition) {
            var innerDatatypeCondition = (DatatypePropertyCondition) innerCondition;
            if (innerDatatypeCondition.getPropertyUri().equalsIgnoreCase(Constants.HAS_LOCAL_NAME_URI)) {
                return innerDatatypeCondition.getDatatypeValue();
            } else {
                return null;
            }
        } else if (innerCondition instanceof PropertyGroupCondition) {
            var innerGroupCondition = (PropertyGroupCondition) innerCondition;
            if (innerGroupCondition.getType() == PropertyConditionType.VALUES) {
                return innerGroupCondition.getContents().stream()
                        .filter(c -> c instanceof DatatypePropertyCondition
                                && ((DatatypePropertyCondition) c).getPropertyUri().equalsIgnoreCase(Constants.HAS_LOCAL_NAME_URI))
                        .map(c -> ((DatatypePropertyCondition) c).getDatatypeValue())
                        .findFirst().orElse(null);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


}
