package models.owlGeneration.restrictionFactories;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.semanticweb.owlapi.model.OWLClassExpression;

import models.ConfigurationException;
import models.owlGeneration.RestrictionFactory;
import models.propertyConditions.DatatypePropertyCondition;

public class DateRestrictionFactory extends DatatypeRestrictionFactory{

	private final RestrictionFactory<DatatypePropertyCondition> restrictionFactory;

	public DateRestrictionFactory(RestrictionFactory<DatatypePropertyCondition> restrictionFactory) {
		this.restrictionFactory = restrictionFactory;		
	}

	@Override
	public OWLClassExpression createRestriction(DatatypePropertyCondition condition) throws ConfigurationException {
		DateTime datetime = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm").parseDateTime(condition.getDatatypeValue().trim());
		
		//10/26/2001 00:00 
		condition.setDatatypeValue("2001-10-26T21:32:52");//datetime.toString());
		return restrictionFactory.createRestriction(condition);
	}

}

