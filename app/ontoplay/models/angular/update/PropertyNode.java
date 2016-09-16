package ontoplay.models.angular.update;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Motasem Alwazir 
 *
 */
public class PropertyNode {
	
	
	private String id;
	private String className;
	private String property;
	private String operator;
	private String inputType;
	private String dataValue;
	private String propertyClass;
	private String objectValue;
	private List<PropertyNode> nodes;
	private List<Annotation> annotation;
	private List<Annotation> objectAnnotations;
	
	
	public PropertyNode() {
		this.id="";
		this.className="";
		this.property="off";
		this.operator="off";
		this.inputType="";
		this.dataValue="";
		this.propertyClass="off";
		this.objectValue="off";
		this.nodes=new ArrayList<PropertyNode>();
		this.annotation=new ArrayList<Annotation>();
		this.objectAnnotations=new ArrayList<Annotation>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getPropertyClass() {
		return propertyClass;
	}

	public void setPropertyClass(String propertyClass) {
		this.propertyClass = propertyClass;
	}

	public String getObjectValue() {
		return objectValue;
	}

	public void setObjectValue(String objectValue) {
		this.objectValue = objectValue;
	}

	public List<PropertyNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<PropertyNode> nodes) {
		this.nodes = nodes;
	}

	public List<Annotation> getAnnotation() {
		return annotation;
	}

	public void setAnnotation(List<Annotation> annotation) {
		this.annotation = annotation;
	}

	public List<Annotation> getObjectAnnotations() {
		return objectAnnotations;
	}

	public void setObjectAnnotations(List<Annotation> objectAnnotations) {
		this.objectAnnotations = objectAnnotations;
	}

	@Override
	public String toString() {
		StringBuilder resultBuilder=new StringBuilder();
		resultBuilder.append("id= ").append(id).append("\n")
					.append("\t").append("className= ").append(className).append("\n")
					.append("\t").append("property= ").append(property).append("\n")
					.append("\t").append("operator= ").append(operator).append("\n")
					.append("\t").append("inputType= ").append(inputType).append("\n")
					.append("\t").append("dataValue= ").append(dataValue).append("\n")
					.append("\t").append("propertyClass= ").append(propertyClass).append("\n")
					.append("\t").append("objectValue= ").append(objectValue).append("\n");
			if(nodes!=null&&nodes.size()>0){
					resultBuilder.append("\t").append("properties ").append("\n").append("///////////////////////////")
					.append("\n");
			for(PropertyNode prop:nodes){
				resultBuilder.append("\t\t").append(prop.toString()).append("\n")
				.append("*************").append("\n");
			}
			resultBuilder.append("///////////////////////////").append("\n");
			}
			return resultBuilder.toString();
					
	}
	
	
	
	
	
	
}
