package models.tan;

import java.util.HashMap;
import java.util.Map;

public class PropertyStatistic {
	
	private String propertyName;
	
	public Map<String, Double> PropValues = new HashMap<String, Double>(); 
	
	public PropertyStatistic(String name)
	{
		this.propertyName = name;
	}
	
	public String getPropertyName()
	{
		return this.propertyName;
	}
	
	public void put(String valueName, Double percentage)
	{
		if(!PropValues.containsKey(valueName))
			PropValues.put(valueName, percentage);
		else
			PropValues.replace(valueName, percentage);
	}
	
	public Map<String, Double> getValues()
	{
		return this.PropValues;
	}
	
	public int size()
	{
		return this.PropValues.size();
	}
}
