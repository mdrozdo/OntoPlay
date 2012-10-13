import java.io.File;

import models.ClassCondition;
import models.owlGeneration.restrictionFactories.DatetimeRestrictionFactoryDecorator;
import models.properties.DateTimeProperty;
import models.properties.OwlDatatypeProperty;
import models.propertyConditions.DatatypePropertyCondition;
import static org.fest.assertions.Assertions.assertThat;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;

import fakes.FakeRestrictionFactory;


public class DatetimeRestrictionFactoryDecoratorTest {

	@Test
	public void forDatetimePropertyEqualsCondition_convertToOwl_ReturnsCreatedOwlClassDescription() throws Exception {
		
		FakeRestrictionFactory fakeRestrictionFactory = new FakeRestrictionFactory();
		DatetimeRestrictionFactoryDecorator decorator = new DatetimeRestrictionFactoryDecorator(fakeRestrictionFactory);
		
		decorator.createRestriction(new DatatypePropertyCondition("http://www.w3.org/2006/time#inXSDDateTime", "equalTo", "10/26/2001 00:00 "));
		
		assertThat(fakeRestrictionFactory.getReceivedCondition().getDatatypeValue()).isEqualTo("2001-10-26T00:00:00");
	}
	
}
