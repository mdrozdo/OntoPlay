package ontoplay.models.owlGeneration;

import org.semanticweb.owlapi.model.OWLDataFactory;

//TODO: Is this needed? I've registered factory in Guice, so probably not necessary
/**
 * Created by michal on 01.12.2016.
 */
public interface IndividualGeneratorFactory {
    IndividualGenerator create(OWLDataFactory factory);
}
