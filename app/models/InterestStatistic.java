package models;

public class InterestStatistic {
	
	public String URI;
	
	public String Name;
	
	public Double Percentage;
	
	public Double Average;
	
	public Double Minimum;
	
	public Double Maximum;
	
	public InterestStatistic(String name, String uri, Double numberOfInterestedConsumers, Double numberOfTotalConsumers,  Double avg, Double min, Double max )
	{
		this.URI = uri;
		this.Name = name;
		this.Average = avg;
		this.Maximum = max;
		this.Minimum = min;
		
		try{
		this.Percentage = numberOfInterestedConsumers*100 / numberOfTotalConsumers;
		}catch(Exception ex){}
	}
	
	public String getColor()
	{
		if(this.Percentage >= 50)
			return "class=success";
		return "";
	}
}
