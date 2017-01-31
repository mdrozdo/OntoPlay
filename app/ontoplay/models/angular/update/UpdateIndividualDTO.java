package ontoplay.models.angular.update;

import java.util.ArrayList;
import java.util.List;

import ontoplay.models.angular.update.Annotation;
import ontoplay.models.angular.update.PropertyNode;
/**
 * 
 * @author Motasem Alwazir
 *
 */
public class UpdateIndividualDTO {
	
	private String individualName;
	private List<PropertyNode> properties;
	private List<Annotation> annotations;

	public UpdateIndividualDTO() {
		properties=new ArrayList<PropertyNode>();
		annotations=new ArrayList<Annotation>();
	}
	
	public List<PropertyNode> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyNode> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		StringBuilder result=new StringBuilder();
		result.append("properties").append("\n");
		
		for(PropertyNode prop:properties){
			result.append("\t").append(prop.toString()).append("\n*********************").append("\n");
			
		}
		result.append("Annotations").append("\n");
		for(Annotation annotation:annotations){
			result.append("\t").append(annotation.toString()).append("\n*********************").append("\n");
			
		}
		
		
		return result.toString();
	}



	public List<Annotation> getAnnotations() {
		return annotations;
	}



	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}
	
	public void addProperty(PropertyNode property){
		try{
			
			int lastId=Integer.parseInt(this.properties.get(this.properties.size()-1).getId());
			lastId+=1;
			property.setId(""+lastId);
		
		}catch(Exception e){
			property.setId("1");
		}
		this.properties.add(property);
	
	}

	public String getIndividualName() {
		return individualName;
	}

	public void setIndividualName(String individualName) {
		this.individualName = individualName;
	}
	
	
	public void addAnnotation( String localName,String uri,String value){
		Annotation annotation=new Annotation();
		annotation.setLocalName(localName);
		annotation.setUri(uri);
		annotation.setValue(value);
		try{
			
			int lastId=this.annotations.get(this.annotations.size()-1).getId();
			lastId+=1;
			annotation.setId(lastId);
		
		}catch(Exception e){
			annotation.setId(1);
		}
		this.annotations.add(annotation);
	
	}
	
}
