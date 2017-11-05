import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import ontoplay.controllers.*;
import ontoplay.controllers.configuration.utils.OntoplayAnnotationUtils;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.OntologyReaderFactory;
import ontoplay.models.ontologyReading.jena.JenaOntologyReaderFactory;
import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import ontoplay.models.ontologyReading.jena.OwlPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.*;
import ontoplay.models.owlGeneration.*;
import ontoplay.models.owlGeneration.restrictionFactories.*;
import ontoplay.models.properties.*;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;
import org.semanticweb.owlapi.model.OWLDataFactory;
import play.Configuration;
import play.Environment;
import play.Logger;

import java.io.File;

/**
 * Created by michal on 22.11.2016.
 */
public class Module extends AbstractModule {

    private final Environment environment;
    private final Configuration configuration;
    public Module(Environment environment, Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        install(new ontoplay.Module(environment, configuration));
        bind(MainTemplate.class).to(OntoPlayMainTemplate.class);
    }

}
