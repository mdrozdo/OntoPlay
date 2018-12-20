package ontoplay.models.owlGeneration.restrictionFactories;

import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyCondition;
import ontoplay.models.PropertyConditionType;
import ontoplay.models.PropertyGroupCondition;
import ontoplay.models.angular.update.Annotation;
import ontoplay.models.owlGeneration.*;
import ontoplay.models.propertyConditions.ClassValueCondition;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MultiplePropertiesRestrictionFactory implements RestrictionFactory<PropertyGroupCondition> {
    private final OWLDataFactory factory;
    private final RestrictionFactoryProvider restrictionFactoryProvider;

    //TODO: Extract some small interface from OWLApiKB just for generating class restrictions
    @Inject
    public MultiplePropertiesRestrictionFactory(RestrictionFactoryProvider restrictionFactoryProvider,
                                                OWLDataFactory factory) {
        this.restrictionFactoryProvider = restrictionFactoryProvider;
        this.factory = factory;
    }

    @Override
    public OWLClassExpression createRestriction(PropertyGroupCondition condition) throws ConfigurationException {
        List<OWLClassExpression> restrictions = new ArrayList<>();

        if(condition == null || condition.getContents() == null){
            throw new IllegalArgumentException("Condition is null or has null contents.");
        }
        for(PropertyCondition cond : condition.getContents()){
            RestrictionFactory<PropertyCondition> restrictionFactory = restrictionFactoryProvider.getRestrictionFactory(cond);
            restrictions.add(restrictionFactory.createRestriction(cond));
        }

        switch(condition.getType()){
            case UNION:
                return factory.getOWLObjectUnionOf(restrictions);
            case INTERSECTION:
                return factory.getOWLObjectIntersectionOf(restrictions);
            default:
                throw new IllegalArgumentException("Condition is of the wrong type for generating a class restriction. Type is: " + condition.getType());
        }
    }

    @Override
    public List<OWLAxiom> createIndividualValue(PropertyGroupCondition condition, OWLIndividual individual) throws ConfigurationException {
        if (condition.getType() != PropertyConditionType.VALUES){
            throw new IllegalArgumentException("Condition is of the wrong type for generating individual values. Type is: " + condition.getType());
        }

        List<OWLAxiom> axioms = new ArrayList<>();

        for(PropertyCondition cond : condition.getContents()){
            RestrictionFactory<PropertyCondition> restrictionFactory = restrictionFactoryProvider.getRestrictionFactory(cond);
            axioms.addAll(restrictionFactory.createIndividualValue(cond, individual));
        }

        return axioms;

        //TODO: Implement annotations?
//        List<Annotation> tempAnnotations = condition.getAnnotations();
//        Set<OWLAnnotation> annotations = new HashSet<>();
//        for (Annotation condtionAnnotation : tempAnnotations) {
//            OWLAnnotationProperty owlAnnotationProperty = factory
//                    .getOWLAnnotationProperty(IRI.create(condtionAnnotation.getUri()));
//
//            annotations.add(factory.getOWLAnnotation(owlAnnotationProperty,
//                    factory.getOWLLiteral(condtionAnnotation.getValue())));
//        }
//
//        if (annotations.size() != 0) {
//            OWLAxiom ax = factory.getOWLObjectPropertyAssertionAxiom(conditionProperty, individual,
//                    nestedIndividual.asOWLAnonymousIndividual(), annotations);
//            result.add(ax);
//        }
    }


}
