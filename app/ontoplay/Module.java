package ontoplay;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import ontoplay.controllers.*;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import ontoplay.models.owlGeneration.*;
import ontoplay.models.owlGeneration.restrictionFactories.*;
import ontoplay.models.properties.*;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import play.Configuration;
import play.Environment;

import java.util.Map;
import java.util.stream.Collectors;

import static com.clarkparsia.owlapiv3.OWL.factory;

/**
 * Created by michal on 22.11.2016.
 */
public class Module extends AbstractModule{

    private Map<String, String> ontoplayProperties;

    public Module(Environment environment, Configuration configuration){
        ontoplayProperties = configuration.subKeys().stream()
                .filter(k->k.startsWith("ontoplay"))
                .collect(Collectors.toMap(k->k, k-> configuration.getString(k)));
    }

    @Override
    protected void configure() {
        Names.bindProperties(binder(), ontoplayProperties);

        bind(OntologyReader.class).to(JenaOwlReader.class).in(Singleton.class);

        bind(new TypeLiteral<RestrictionFactory<IndividualValueCondition>>(){}).to(IndividualValueRestrictionFactory.class).in(Singleton.class);
        bind(new TypeLiteral<RestrictionFactory<ClassValueCondition>>(){}).to(ClassValueRestrictionFactory.class);


        bind(new TypeLiteral<PropertyConditionRenderer<OwlObjectProperty>>(){}).to(ObjectPropertyRenderer.class);

//        install(new FactoryModuleBuilder()
//                .implement(ClassRestrictionGenerator.class, ClassRestrictionGenerator.class)
//                .build(ClassRestrictionGeneratorFactory.class));
//        install(new FactoryModuleBuilder()
//                .implement(IndividualGenerator.class, IndividualGenerator.class)
//                .build(IndividualGeneratorFactory.class));
    }

    @Provides @Singleton
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
    private PropertyConditionRenderer<StringProperty> createStringPropertyRenderer() {
        DatatypePropertyRenderer stringPropertyRenderer = new DatatypePropertyRenderer();
        stringPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true,  new SimpleDatatypePropertyValueRenderer());
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


    @Provides @Singleton
    private OWLDataFactory createDataFactory(OntologyGenerator gen){
        return gen.getOwlApiFactory();
    }

    //@Provides

}
