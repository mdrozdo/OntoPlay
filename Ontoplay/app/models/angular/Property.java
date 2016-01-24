package models.angular;

public class Property {
	private String id;
	private String className;
	private String property;
	private String propertyType;
	private String operator;
	private String dataValue;
	private String propertyClass;
	private String objectValue;
	private Property[] nodes;
	
	public Property(String id, String className, String property, String propertyType, String operator,
			String dataValue, String propertyClass, String objectValue, Property[] nodes) {
		super();
		this.id = id;
		this.className = className;
		this.property = property;
		this.propertyType = propertyType;
		this.operator = operator;
		this.dataValue = dataValue;
		this.propertyClass = propertyClass;
		this.objectValue = objectValue;
		this.nodes = nodes;
	}

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

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
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
	
	
	
	
}
