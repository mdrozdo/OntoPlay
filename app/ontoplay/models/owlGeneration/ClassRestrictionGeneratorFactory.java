package ontoplay.models.owlGeneration;

import ontoplay.models.owlGeneration.ClassRestrictionGenerator;
import org.semanticweb.owlapi.model.OWLDataFactory;


//TODO: Is this needed? I've registered factory in Guice, so probably not necessary
/**
 * Created by michal on 01.12.2016.
 */
public interface ClassRestrictionGeneratorFactory {
    ClassRestrictionGenerator create(OWLDataFactory factory);
}
