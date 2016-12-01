package ontoplay.jobs;

import ontoplay.models.owlGeneration.*;
import org.semanticweb.owlapi.model.OWLDataFactory;
import ontoplay.controllers.DatatypePropertyRenderer;
import ontoplay.controllers.DateTimePropertyValueRenderer;
import ontoplay.controllers.ObjectPropertyRenderer;
import ontoplay.controllers.PropertyConditionRenderer;
import ontoplay.controllers.SimpleDatatypePropertyValueRenderer;
import ontoplay.models.owlGeneration.restrictionFactories.ClassValueRestrictionFactory;
import ontoplay.models.owlGeneration.restrictionFactories.DatatypeRestrictionFactory;
import ontoplay.models.owlGeneration.restrictionFactories.DatetimeRestrictionFactoryDecorator;
import ontoplay.models.owlGeneration.restrictionFactories.EqualToRestrictionFactory;
import ontoplay.models.owlGeneration.restrictionFactories.GreaterThanRestrictionFactory;
import ontoplay.models.owlGeneration.restrictionFactories.IndividualValueRestrictionFactory;
import ontoplay.models.owlGeneration.restrictionFactories.LessThanRestrictionFactory;
import ontoplay.models.properties.DateTimeProperty;
import ontoplay.models.properties.FloatProperty;
import ontoplay.models.properties.IntegerProperty;
import ontoplay.models.properties.OwlObjectProperty;
import ontoplay.models.properties.StringProperty;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;

//@OnApplicationStart

//TODO: Split into parts related to rendering and to generating restrictions
//TODO: Make the RestrictionFactory and PropertyConditionRenderer non-static
//TODO: Move the logic from here into the module - the factory registering should become Guice registrations.
// Providers should get the injector and ask it for implementations. See https://luisfsgoncalves.wordpress.com/2010/09/08/generic-bindings-with-guice/
public class PropertyTypeConfiguration {
	private OWLDataFactory factory;

	public void doJob() throws Exception {
		factory = OntologyGenerator.getOwlApiFactory();
				
//		RestrictionFactoryProvider.registerRestrictionTypeFactory(DatatypePropertyCondition.class, createDatatypeRestrictionFactory());
//		RestrictionFactoryProvider.registerRestrictionTypeFactory(IndividualValueCondition.class, new IndividualValueRestrictionFactory(factory));
//		RestrictionFactoryProvider.registerRestrictionTypeFactory(ClassValueCondition.class,
//				new ClassValueRestrictionFactory(getClassRestrictionGenerator(), getIndividualGenerator(), factory));
		
		PropertyConditionRenderer.registerPropertyTypeRenderer(StringProperty.class, createStringPropertyRenderer());
		PropertyConditionRenderer.registerPropertyTypeRenderer(IntegerProperty.class, createIntegerPropertyRenderer());
		PropertyConditionRenderer.registerPropertyTypeRenderer(FloatProperty.class, createFloatPropertyRenderer());
		PropertyConditionRenderer.registerPropertyTypeRenderer(DateTimeProperty.class, createDateTimePropertyRenderer());
		PropertyConditionRenderer.registerPropertyTypeRenderer(OwlObjectProperty.class, new ObjectPropertyRenderer());
	}

	private PropertyConditionRenderer createStringPropertyRenderer() {
		DatatypePropertyRenderer stringPropertyRenderer = new DatatypePropertyRenderer();
		stringPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true,  new SimpleDatatypePropertyValueRenderer());
		return stringPropertyRenderer;
	}

	private DatatypePropertyRenderer createIntegerPropertyRenderer() {
		DatatypePropertyRenderer integerPropertyRenderer = new DatatypePropertyRenderer();
		integerPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true, new SimpleDatatypePropertyValueRenderer());
		integerPropertyRenderer.registerPropertyOperator("greaterThan", "is greater than ", new SimpleDatatypePropertyValueRenderer());
		integerPropertyRenderer.registerPropertyOperator("lessThan", "is less than ", new SimpleDatatypePropertyValueRenderer());
		return integerPropertyRenderer;
	}
	
	private DatatypePropertyRenderer createFloatPropertyRenderer() {
		DatatypePropertyRenderer floatPropertyRenderer = new DatatypePropertyRenderer();
		floatPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true, new SimpleDatatypePropertyValueRenderer());
		floatPropertyRenderer.registerPropertyOperator("greaterThan", "is greater than ", new SimpleDatatypePropertyValueRenderer());
		floatPropertyRenderer.registerPropertyOperator("lessThan", "is less than ", new SimpleDatatypePropertyValueRenderer());
		return floatPropertyRenderer;
	}
	
	private DatatypePropertyRenderer createDateTimePropertyRenderer() {
		DatatypePropertyRenderer floatPropertyRenderer = new DatatypePropertyRenderer();
		floatPropertyRenderer.registerPropertyOperator("equalTo", "is equal to ", true, new DateTimePropertyValueRenderer());
		floatPropertyRenderer.registerPropertyOperator("greaterThan", "is greater than ", new DateTimePropertyValueRenderer());
		floatPropertyRenderer.registerPropertyOperator("lessThan", "is less than ", new DateTimePropertyValueRenderer());
		return floatPropertyRenderer;
	}

}
