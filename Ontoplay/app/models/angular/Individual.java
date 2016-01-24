package models.angular;

public class Individual {

	private Property[] properties;

	
	
	public Individual(Property[] properties) {
		super();
		this.properties = properties;
	}
	
	

	public Individual() {
		super();
	}



	public Property[] getProperties() {
		return properties;
	}

	public void setProperties(Property[] properties) {
		this.properties = properties;
	}
	
	
	
}
