package ontoplay.models.owlGeneration.restrictionFactories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ontoplay.models.ConfigurationException;
import ontoplay.models.angular.update.Annotation;
import ontoplay.models.owlGeneration.*;
import ontoplay.models.propertyConditions.ClassValueCondition;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;

import javax.inject.Inject;


public class ClassValueRestrictionFactory implements RestrictionFactory<ClassValueCondition> {
	private final OWLDataFactory factory;
	private final ClassRestrictionGenerator classRestrictionGenerator;
	private final IndividualGenerator individualGenerator;
	
	//TODO: Extract some small interface from OWLApiKB just for generating class restrictions
	@Inject
	public ClassValueRestrictionFactory(ClassRestrictionGeneratorFactory classRestrictionGeneratorFactory, IndividualGeneratorFactory individualGeneratorFactory,
										OWLDataFactory factory) {
		this.classRestrictionGenerator = classRestrictionGeneratorFactory.create(factory);
		this.individualGenerator = individualGeneratorFactory.create(factory);
		this.factory = factory;
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
		OWLIndividual nestedIndividual = factory.getOWLAnonymousIndividual();

		List<OWLAxiom> nestedAxioms = individualGenerator.createPropertyAxioms(nestedIndividual, condition.getClassConstraintValue());
		OWLObjectPropertyAssertionAxiom assertion = factory.getOWLObjectPropertyAssertionAxiom(conditionProperty, individual, nestedIndividual);
		ArrayList<OWLAxiom> result = new ArrayList<OWLAxiom>();
		result.addAll(nestedAxioms);
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
					nestedIndividual.asOWLAnonymousIndividual(), annotations);
			result.add(ax);
		}		
		return result;
	}
	

}
