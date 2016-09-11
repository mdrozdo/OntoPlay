package ontoplay.models.owlGeneration.restrictionFactories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import ontoplay.models.ConfigurationException;
import ontoplay.models.angular.update.Annotation;
import ontoplay.models.owlGeneration.RestrictionFactory;
import ontoplay.models.propertyConditions.IndividualValueCondition;


public class IndividualValueRestrictionFactory extends RestrictionFactory<IndividualValueCondition> {
	private final OWLDataFactory factory;
	
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

		OWLObjectPropertyAssertionAxiom assertion = factory.getOWLObjectPropertyAssertionAxiom(conditionProperty, individual, valueIndividual);
		ArrayList<OWLAxiom> result = new ArrayList<OWLAxiom>();
		result.add(assertion);
		
		List<Annotation> tempAnnotations=condition.getAnnotations();
		Set<OWLAnnotation> annotations = new HashSet<>();
       for(Annotation condtionAnnotation:tempAnnotations){
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
