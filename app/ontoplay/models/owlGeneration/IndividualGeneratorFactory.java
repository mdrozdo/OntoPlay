package ontoplay.models.owlGeneration;

import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Created by michal on 01.12.2016.
 */
public interface IndividualGeneratorFactory {
    IndividualGenerator create(OWLDataFactory factory);
}
