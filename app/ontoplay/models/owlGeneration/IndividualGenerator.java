package ontoplay.models.owlGeneration;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.Assisted;
import ontoplay.models.ClassCondition;
import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyValueCondition;
import ontoplay.models.angular.update.Annotation;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

import javax.inject.Inject;

public class IndividualGenerator{

	private RestrictionFactoryProvider restrictionFactoryProvider;
	private final OWLDataFactory factory;

	@Inject
	public IndividualGenerator(RestrictionFactoryProvider restrictionFactoryProvider, @Assisted OWLDataFactory factory){
		this.restrictionFactoryProvider = restrictionFactoryProvider;
		this.factory = factory;
	}
	
	public List<OWLAxiom> convertToOntIndividual(String individualUri, ClassCondition condition) throws ConfigurationException {
	//a new OWLNamedIndividual object, or a cached one depending on policies
		OWLIndividual individual = factory.getOWLNamedIndividual(IRI.create(individualUri));
		
		return createPropertyAxioms(individual, condition);
	}

	public List<OWLAxiom> createPropertyAxioms(OWLIndividual individual, ClassCondition condition) throws ConfigurationException {
		ArrayList<OWLAxiom> individualAxioms = new ArrayList<OWLAxiom>();

		//get individual declaration axioms
		//true if this individual is an instance of OWLNamedIndividual because it is a named individuals, otherwise false
		
		if (individual.isNamed()) {
			OWLDeclarationAxiom individualAxiom = factory.getOWLDeclarationAxiom(individual.asOWLNamedIndividual());
			
			individualAxioms.add(individualAxiom);
		}
		
		
		condition.setClassUri(condition.getClassUri());
		OWLClass conditionClass = factory.getOWLClass(IRI.create(condition.getClassUri()));
		
		//returns a class assertion axiom
		//get class assertion axioms
		OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(conditionClass, individual);
				
		individualAxioms.add(classAssertionAxiom);
		
		//properies axioms
		for (PropertyValueCondition cond : condition.getPropertyConditions()) {
			RestrictionFactory restrictionFactory = restrictionFactoryProvider.getRestrictionFactory(cond);
			List<OWLAxiom> propertyAxioms = restrictionFactory.createIndividualValue(cond, individual);
			individualAxioms.addAll(propertyAxioms);
		}
		for(Annotation ann:condition.getAnnotations()){
		OWLAnnotationProperty owlAnnotationProperty = factory.getOWLAnnotationProperty(IRI.create(ann.getUri()));
		OWLAxiom annotationAxiom;
		if(!individual.isAnonymous()){
			annotationAxiom=factory.getOWLAnnotationAssertionAxiom(owlAnnotationProperty, individual.asOWLNamedIndividual().getIRI(), factory.getOWLLiteral(ann.getValue()));
		}else{
			annotationAxiom=factory.getOWLAnnotationAssertionAxiom(owlAnnotationProperty, individual.asOWLAnonymousIndividual(), factory.getOWLLiteral(ann.getValue()));
		}
			individualAxioms.add(annotationAxiom);
		
		}
		
		return individualAxioms;
	}
}
