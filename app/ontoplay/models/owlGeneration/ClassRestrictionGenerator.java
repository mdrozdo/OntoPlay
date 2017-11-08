package ontoplay.models.owlGeneration;

import com.google.inject.assistedinject.Assisted;
import ontoplay.models.ClassCondition;
import ontoplay.models.ConfigurationException;
import ontoplay.models.PropertyValueCondition;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class ClassRestrictionGenerator {

    private final OWLDataFactory factory;
    private final RestrictionFactoryProvider restrictionFactoryProvider;

    @Inject
    public ClassRestrictionGenerator(RestrictionFactoryProvider restrictionFactoryProvider, @Assisted OWLDataFactory factory) {
        this.restrictionFactoryProvider = restrictionFactoryProvider;
        this.factory = factory;
    }

    public OWLClassExpression convertToOntClass(String classUri, ClassCondition condition) throws ConfigurationException {
        condition.setClassUri(condition.getClassUri());
        OWLClass conditionClass = factory.getOWLClass(IRI.create(condition.getClassUri()));

        Set<OWLClassExpression> intersectionElements = new HashSet<OWLClassExpression>();
        intersectionElements.add(conditionClass);

        for (PropertyValueCondition cond : condition.getPropertyConditions()) {
            RestrictionFactory restrictionFactory = restrictionFactoryProvider.getRestrictionFactory(cond);
            OWLClassExpression restriction = restrictionFactory.createRestriction(cond);
            intersectionElements.add(restriction);
        }

        OWLClassExpression intersection = factory.getOWLObjectIntersectionOf(intersectionElements);

        return intersection;
    }
}
