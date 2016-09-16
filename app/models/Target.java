package models;

import java.util.ArrayList;
import java.util.HashMap;

import play.Logger.ALogger;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class Target {

	private IndividualViewModel individual;
	private String targetUri;
	
	public ArrayList<InterestStatistic> interests = new ArrayList<InterestStatistic>();
	public HashMap<String,PropertyStatistic> properties = new HashMap<String,PropertyStatistic>();
	
	public Target(IndividualViewModel individual)
	{
		
		this.individual = individual;
		this.targetUri = individual.getUri();
		Query();
	}
	
	private Double getNumberOfTotalConsumers()
	{
		String query = StringConstants.getConsumerCountQuery(this.targetUri);
		QueryExecuter q = new QueryExecuter();
		ResultSet result = QueryExecuter.connection.executeQuery(query);
		while (result.hasNext()) {
			
			QuerySolution row = result.nextSolution();
			
			ALogger log = play.Logger.of("application");
			log.info( "\n" + row.toString() + "\n");
			
			return row.getLiteral("count").getDouble();
		}
		
		return (double)0;
	}
	
	private void Query()
	{
		QueryInterests();
		QueryProperties();
	}
	
	private void QueryProperties()
	{
		try
		{
		Double numberOfTotalConsumers = getNumberOfTotalConsumers();
		String query = StringConstants.getPropertyQuery(this.targetUri);
		QueryExecuter q = new QueryExecuter();
		ResultSet result = QueryExecuter.connection.executeQuery(query);
		
			try
			{
				while (result.hasNext()) {
					QuerySolution row = result.nextSolution();
					
					ALogger log = play.Logger.of("application");
					log.info( "\n" + row.toString() + "\n");
					
					String name = row.getLiteral("name").getString();
					String value = row.getLiteral("value").getString();
					Double count = row.getLiteral("count").getDouble();
					
					AddProperty(name, value, count, numberOfTotalConsumers);
				}
			}
			catch(Exception ex)
			{}
		
		}
		catch(Exception ex)
		{}
	}
	
	private void AddProperty(String name,
			String value, Double count, Double numberOfTotalConsumers) {
		PropertyStatistic property = null;
		if(properties.containsKey(name))
		{
			property = properties.get(name);
		}
		else
		{
			property = new PropertyStatistic(name);
			properties.put(name, property);
		}
		property.put(value, count*100/numberOfTotalConsumers);
	}

	private void QueryInterests()
	{
		try
		{
		Double numberOfTotalConsumers = getNumberOfTotalConsumers();
		String query = StringConstants.getInterestsQuery(this.targetUri);
		QueryExecuter q = new QueryExecuter();
		ResultSet result = QueryExecuter.connection.executeQuery(query);	
		
			try
			{
				while (result.hasNext()) {
					QuerySolution row = result.nextSolution();
					
					ALogger log = play.Logger.of("application");
					log.info( "\n" + row.toString() + "\n");
					
					String name = row.getLiteral("name").getString();
					Double numberOfInterestedConsumers = row.getLiteral("count").getDouble();
					Double avg = row.getLiteral("avg").getDouble();
					Double sum = row.getLiteral("sum").getDouble();
					Double min = row.getLiteral("min").getDouble();
					Double max = row.getLiteral("max").getDouble();
					String uri = row.getResource("interest").getLocalName();
					
					InterestStatistic interest = new InterestStatistic(name, uri, numberOfInterestedConsumers, numberOfTotalConsumers, avg, min, max);
					this.interests.add(interest);
				}
			}
			catch(Exception ex)
			{}
		}
		catch(Exception ex)
		{}
	}
	
	public ArrayList<InterestStatistic> getInterests()
	{return this.interests;}
	
	public String getOfferURL()
	{
		if(offerSent())
		{
			return "<a href=# class=\"btn btn-success\" disabled=\"true\">Offer Sent</a>";
		}else{
			return "<a href="+getOfferURI()+" class=\"btn btn-warning\" disabled=\"true\">Send Offer</a>";
		}
	}
	
	public String getOfferURI()
	{
		if(!this.individual.functionalObjectValues.containsKey("hasOffer"))
			return "";
		else
			return "/sendOffer/" + this.individual.functionalObjectValues.get("hasOffer").getLocalName() +"/"+ this.individual.getLocalName();
	}
	
	public boolean hasOffer()
	{
		return this.individual.functionalObjectValues.containsKey("hasOffer");
	}
	
	public boolean offerSent()
	{
		if(!this.individual.functionalObjectValues.containsKey("hasOffer"))
			return false;
		if(!this.individual.functionalDataValues.containsKey("isOfferSent"))
			return false;
		return this.individual.functionalDataValues.get("isOfferSent").getBoolean();
	}
	
}
