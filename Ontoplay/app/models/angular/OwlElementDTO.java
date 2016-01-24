package models.angular;

public class OwlElementDTO {
	private String uri;
	private String localName;
	
	
	
	public OwlElementDTO(String uri, String localName) {
		super();
		this.uri = uri;
		this.localName = localName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	

}
