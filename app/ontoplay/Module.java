package ontoplay;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import ontoplay.FileOntoplayConfig;
import ontoplay.OntoplayConfig;
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

    private final OntoplayConfig configuration;

    public Module(Environment environment, Configuration configuration) {
        File ontoPlayConfigFile = environment.getFile(configuration.getString("ontoplay.config"));

        Logger.info("Loading ontoplay config from: " + ontoPlayConfigFile.getAbsolutePath());

        this.configuration = new FileOntoplayConfig(ontoPlayConfigFile);

    }

    public Module(OntoplayConfig config) {
        this.configuration = config;
    }

    @Override
    protected void configure() {
        bind(OntologyReader.class).to(JenaOwlReader.class).in(Singleton.class);

        bind(new TypeLiteral<RestrictionFactory<IndividualValueCondition>>() {
        }).to(IndividualValueRestrictionFactory.class).in(Singleton.class);
        bind(new TypeLiteral<RestrictionFactory<ClassValueCondition>>() {
        }).to(ClassValueRestrictionFactory.class);

        bind(new TypeLiteral<PropertyConditionRenderer<OwlObjectProperty>>() {
        }).to(ObjectPropertyRenderer.class);

        bind(OntologyReaderFactory.class).to(JenaOntologyReaderFactory.class);
        install(new FactoryModuleBuilder()
                .implement(ClassRestrictionGenerator.class, ClassRestrictionGenerator.class)
                .build(ClassRestrictionGeneratorFactory.class));
        install(new FactoryModuleBuilder()
                .implement(IndividualGenerator.class, IndividualGenerator.class)
                .build(IndividualGeneratorFactory.class));

        //bind(MainTemplate.class).to(OntoPlayMainTemplate.class);

//        bind(MainTemplate.class).to(OntoPlayMainTemplate.class); //needs to be defined in the main application's module
//        install(new FactoryModuleBuilder()
//                .implement(OntologyReader.class, JenaOwlReader.class)
//                .build(OntologyReaderFactory.class));


    }

    @Provides
    private OntoplayConfig createConfig() {
        return configuration;
    }

    @Provides
    @Singleton
    private RestrictionFactory<DatatypePropertyCondition> createDatatypeRestrictionFactory(OWLDataFactory factory) throws Exception {

        DatatypeRestrictionFactory topLevelFactory = new DatatypeRestrictionFactory();
        topLevelFactory.registerOperatorRestrictionFactory(StringProperty.class, "equalTo", new EqualToRestrictionFactory("http://www.w3.org/2001/XMLSchema#string", factory));
        topLevelFactory.registerOperatorRestrictionFactory(IntegerProperty.class, "equalTo", new EqualToRestrictionFactory("http://www.w3.org/2001/XMLSchema#integer", factory));
        topLevelFactory.registerOperatorRestrictionFactory(IntegerProperty.class, "lessThan", new LessThanRestrictionFactory("http://www.w3.org/2001/XMLSchema#integer", factory));
        topLevelFactory.registerOperatorRestrictionFactory(IntegerProperty.class, "greaterThan", new GreaterThanRestrictionFactory("http://www.w3.org/2001/XMLSchema#integer", factory));
        topLevelFactory.registerOperatorRestrictionFactory(FloatProperty.class, "equalTo", new EqualToRestrictionFactory("http://www.w3.org/2001/XMLSchema#float", factory));
        topLevelFactory.registerOperatorRestrictionFactory(FloatProperty.class, "lessThan", new LessThanRestrictionFactory("http://www.w3.org/2001/XMLSchema#float", factory));
        topLevelFactory.registerOperatorRestrictionFactory(FloatProperty.class, "greaterThan", new GreaterThanRestrictionFactory("http://www.w3.org/2001/XMLSchema#float", factory));
        topLevelFactory.registerOperatorRestrictionFactory(DateTimeProperty.class, "equalTo", new DatetimeRestrictionFactoryDecorator(new EqualToRestrictionFactory("http://www.w3.org/2001/XMLSchema#dateTime", factory)));
        topLevelFactory.registerOperatorRestrictionFactory(DateTimeProperty.class, "lessThan",
                new DatetimeRestrictionFactoryDecorator(new LessThanRestrictionFactory("http://www.w3.org/2001/XMLSchema#dateTime", factory)));
        topLevelFactory.registerOperatorRestrictionFactory(DateTimeProperty.class, "greaterThan",
                new DatetimeRestrictionFactoryDecorator(new GreaterThanRestrictionFactory("http://www.w3.org/2001/XMLSchema#dateTime", factory)));

        return topLevelFactory;
    }

    @Provides
    @Singleton
    private OwlPropertyFactory createOwlPropertyFactory() {
        OwlPropertyFactory topLevelFactory = new OwlPropertyFactory();
        topLevelFactory.registerPropertyFactory(new IntegerPropertyFactory());
        topLevelFactory.registerPropertyFactory(new FloatPropertyFactory());
        topLevelFactory.registerPropertyFactory(new DateTimePropertyFactory());
        topLevelFactory.registerPropertyFactory(new StringPropertyFactory());
        topLevelFactory.registerPropertyFactory(new ObjectPropertyFactory());
        return topLevelFactory;
    }

    @Provides
    @Singleton
    private JenaOwlReader createJenaReader(OwlPropertyFactory owlPropertyFactory) {
        return new JenaOwlReader(owlPropertyFactory, configuration, configuration.getIgnorePropertiesWithNoDomain());
    }

    @Provides
    @Singleton
    private OntoplayAnnotationUtils createOntoplayAnnotationUtils() {
        String uri = configuration.getAnnotationsFilePath();
        return new OntoplayAnnotationUtils(uri);
    }


    @Provides
    private PropertyConditionRenderer<StringProperty> createStringPropertyRenderer() {
        DatatypePropertyRenderer stringPropertyRenderer = new DatatypePropertyRenderer();
        stringPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true, new SimpleDatatypePropertyValueRenderer());
        return stringPropertyRenderer;
    }

    @Provides
    private PropertyConditionRenderer<IntegerProperty> createIntegerPropertyRenderer() {
        DatatypePropertyRenderer integerPropertyRenderer = new DatatypePropertyRenderer();
        integerPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true, new SimpleDatatypePropertyValueRenderer());
        integerPropertyRenderer.registerPropertyOperator("greaterThan", "is greater than ", new SimpleDatatypePropertyValueRenderer());
        integerPropertyRenderer.registerPropertyOperator("lessThan", "is less than ", new SimpleDatatypePropertyValueRenderer());
        return integerPropertyRenderer;
    }

    @Provides
    private PropertyConditionRenderer<FloatProperty> createFloatPropertyRenderer() {
        DatatypePropertyRenderer floatPropertyRenderer = new DatatypePropertyRenderer();
        floatPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true, new SimpleDatatypePropertyValueRenderer());
        floatPropertyRenderer.registerPropertyOperator("greaterThan", "is greater than ", new SimpleDatatypePropertyValueRenderer());
        floatPropertyRenderer.registerPropertyOperator("lessThan", "is less than ", new SimpleDatatypePropertyValueRenderer());
        return floatPropertyRenderer;
    }

    @Provides
    private PropertyConditionRenderer<DateTimeProperty> createDateTimePropertyRenderer() {
        DatatypePropertyRenderer floatPropertyRenderer = new DatatypePropertyRenderer();
        floatPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true, new DateTimePropertyValueRenderer());
        floatPropertyRenderer.registerPropertyOperator("greaterThan", "is greater than ", new DateTimePropertyValueRenderer());
        floatPropertyRenderer.registerPropertyOperator("lessThan", "is less than ", new DateTimePropertyValueRenderer());
        return floatPropertyRenderer;
    }


    @Provides
    private OWLDataFactory createDataFactory(OntologyGenerator gen) {
        return gen.getOwlApiFactory();
    }

    //@Provides

}
