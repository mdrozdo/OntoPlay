package ontoplay.models.angular.update;

import java.util.Arrays;

public class Property {
	
	/*
	 * json object
    "id": 1,
    "className": "Consumer",
    "property": "http://www.tan.com#hasGender",
    "operator": "equalToIndividual",
    "inputType": "object",
    "dataValue": "",
    "propertyClass": "off",
    "objectValue": "off",
    "nodes": [],
    "annotations": [],
    "objectAnnotations": []
	*/
	
	private String id;
	private String className;
	private String property;
	private String operator;
	private String inputType;
	private String dataValue;
	private String propertyClass;
	private String objectValue;
	private Property[] nodes;
	private Annotation[] annotation;
	private Annotation[] objectAnnotations;
	
	
	public Property() {
		super();
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

	public Property[] getNodes() {
		return nodes;
	}

	public void setNodes(Property[] nodes) {
		this.nodes = nodes;
	}

	public Annotation[] getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation[] annotation) {
		this.annotation = annotation;
	}

	public Annotation[] getObjectAnnotations() {
		return objectAnnotations;
	}

	public void setObjectAnnotations(Annotation[] objectAnnotations) {
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
			if(nodes!=null&&nodes.length>0){
					resultBuilder.append("\t").append("properties ").append("\n").append("///////////////////////////")
					.append("\n");
			for(Property prop:nodes){
				resultBuilder.append("\t\t").append(prop.toString()).append("\n")
				.append("*************").append("\n");
			}
			resultBuilder.append("///////////////////////////").append("\n");
			}
			return resultBuilder.toString();
					
	}
	
	
	
	
	
	
}
