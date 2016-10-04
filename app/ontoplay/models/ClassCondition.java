package ontoplay.models;

import java.util.ArrayList;
import java.util.List;

import ontoplay.models.angular.update.Annotation;

public class ClassCondition {
	private String classUri;
	private List<PropertyValueCondition> propertyConditions = new ArrayList<PropertyValueCondition>();
	private List<Annotation> annotations=new ArrayList<Annotation>();

	public ClassCondition(){
		
	}
	
	public ClassCondition(String classUri){
		setClassUri(classUri);
	}

		
	public String getClassUri() {
		return classUri;
	}
	
	public void setClassUri(String classUri){
				this.classUri=classUri;
	}

	public List<PropertyValueCondition> getPropertyConditions() {
		return propertyConditions;
	}

	public void addProperty(PropertyValueCondition propertyCondition) {		
		propertyConditions.add(propertyCondition);
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}
}
