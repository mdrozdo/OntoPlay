import fakes.FakeRestrictionFactory;
import ontoplay.models.owlGeneration.restrictionFactories.DatetimeRestrictionFactoryDecorator;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DatetimeRestrictionFactoryDecoratorTest {

	@Test
	public void forDatetimePropertyEqualsCondition_convertToOwl_ReturnsCreatedOwlClassDescription() throws Exception {
		
		FakeRestrictionFactory fakeRestrictionFactory = new FakeRestrictionFactory();
		DatetimeRestrictionFactoryDecorator decorator = new DatetimeRestrictionFactoryDecorator(fakeRestrictionFactory);
		
		decorator.createRestriction(new DatatypePropertyCondition("http://www.w3.org/2006/time#inXSDDateTime", "equalTo", "10/26/2001 00:00 "));
		
		assertThat(fakeRestrictionFactory.getReceivedCondition().getDatatypeValue()).isEqualTo("2001-10-26T00:00:00");
	}
	
}
