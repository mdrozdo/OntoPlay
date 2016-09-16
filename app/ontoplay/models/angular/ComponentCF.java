package ontoplay.models.angular;
/**
 * Represent the attributes of the Component Element in the annotation XML configuration file
 * @author Motasem Alwazir
 *
 */

public class ComponentCF {
	private String componentId;
	private String componentIri;
	private String componentName;
	private String componentType;
	private String inputType;
	
	
	
	/**
	 * @return the componentId
	 */
	public String getComponentId() {
		return componentId;
	}
	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(String componentId) {
		if(componentId==null)
			componentId="";
		this.componentId = componentId;
	}
	/**
	 * @return the componentIri
	 */
	public String getComponentIri() {
		return componentIri;
	}
	/**
	 * @param componentIri the componentIri to set
	 */
	public void setComponentIri(String componentIri) {
		if(componentIri==null)
			componentIri="";
		this.componentIri = componentIri;
	}
	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}
	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName) {
		if(componentName==null)
			componentName="";
		this.componentName = componentName;
	}
	/**
	 * @return the componentType
	 */
	public String getComponentType() {
		return componentType;
	}
	/**
	 * @param componentType the componentType to set
	 */
	public void setComponentType(String componentType) {
		if(componentType==null)
			componentType="";
		this.componentType = componentType;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		if(componentType==null)
			componentType="text";
		this.inputType = inputType;
	}
	
}
