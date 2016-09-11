package ontoplay.models.angular.update;
/**
 * Represent annotation object on the front end
 * @author Motasem Alwazir
 *
 */

/*
 		"id": 1,
        "localName": "label",
        "value": "123",
        "uri": "http://www.w3.org/2000/01/rdf-schema#label"
 */
public class Annotation {
	private int id;
	private String localName;
	private String uri;
	private String value;
	
	
	
	
	public Annotation() {
		super();
	}

	public Annotation(int id, String localName, String uri, String value) {
		this.id = id;
		this.localName = localName;
		this.uri = uri;
		this.value = value;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder resultBuilder=new StringBuilder();
		resultBuilder.append("id: ").append(id).append("\n");
		resultBuilder.append("\t").append("localName: ").append(localName).append("\n");
		resultBuilder.append("\t").append("value: ").append(value).append("\n");
		return resultBuilder.toString();
		
	}
	
	
	
	

}
