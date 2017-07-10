package ontoplay.models.angular;

/**
 * Annotation data ransfer object. Used in two places : <ul>
 * <li>Annotation configuration</li>
 * <li>Adding Annotation to object</li>
 * </ul>
 *
 * @author Motasem Alwazir
 */
public class AnnotationDTO extends OwlElementDTO {
    private String inputType;
    private boolean isDefault = false;

    public AnnotationDTO(String uri, String localName) {
        super(uri, localName);
    }

    public AnnotationDTO() {
    }

    /**
     * @return the inputType
     */
    public String getInputType() {
        return inputType;
    }

    /**
     * @param inputType the inputType to set
     */
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    /**
     * @return the isDefault
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * @param isDefault the isDefault to set
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}
