package ontoplay.jobs;

import org.semanticweb.owlapi.model.OWLDataFactory;
import ontoplay.controllers.DatatypePropertyRenderer;
import ontoplay.controllers.DateTimePropertyValueRenderer;
import ontoplay.controllers.ObjectPropertyRenderer;
import ontoplay.controllers.PropertyConditionRenderer;
import ontoplay.controllers.SimpleDatatypePropertyValueRenderer;
import ontoplay.models.owlGeneration.IndividualGenerator;
import ontoplay.models.owlGeneration.OntologyGenerator;
import ontoplay.models.owlGeneration.RestrictionFactory;
import ontoplay.models.owlGeneration.ClassRestrictionGenerator;
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
public class PropertyTypeConfiguration {
	private OWLDataFactory factory;

	public void doJob() throws Exception {
		new OntologyGeneratorConfiguration().doJob();
		factory = OntologyGenerator.getOwlApiFactory();		
				
		RestrictionFactory.registerRestrictionTypeFactory(DatatypePropertyCondition.class, createDatatypeRestrictionFactory());
		RestrictionFactory.registerRestrictionTypeFactory(IndividualValueCondition.class, new IndividualValueRestrictionFactory(factory));
		RestrictionFactory.registerRestrictionTypeFactory(ClassValueCondition.class, 
				new ClassValueRestrictionFactory(getClassRestrictionGenerator(), getIndividualGenerator(), factory));
		
		PropertyConditionRenderer.registerPropertyTypeRenderer(StringProperty.class, createStringPropertyRenderer());
		PropertyConditionRenderer.registerPropertyTypeRenderer(IntegerProperty.class, createIntegerPropertyRenderer());
		PropertyConditionRenderer.registerPropertyTypeRenderer(FloatProperty.class, createFloatPropertyRenderer());
		PropertyConditionRenderer.registerPropertyTypeRenderer(DateTimeProperty.class, createDateTimePropertyRenderer());
		PropertyConditionRenderer.registerPropertyTypeRenderer(OwlObjectProperty.class, new ObjectPropertyRenderer());
	}

	private IndividualGenerator getIndividualGenerator() {
		return OntologyGenerator.getGlobalInstance().getIndividualGenerator();
	}

	private ClassRestrictionGenerator getClassRestrictionGenerator() {
		return OntologyGenerator.getGlobalInstance().getClassRestrictionGenerator();
	}

	private DatatypeRestrictionFactory createDatatypeRestrictionFactory() throws Exception {
		
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
