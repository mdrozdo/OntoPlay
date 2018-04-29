package ontoplay.models.angular;

/**
 * @author Motasem Alwazir
 */
public class OwlElementDTO {
    private String uri;
    private String localName;


    public OwlElementDTO(String uri, String localName) {
        this.uri = uri;
        this.localName = localName;
    }

    public OwlElementDTO() {
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof OwlElementDTO)) return false;
        OwlElementDTO otherMyClass = (OwlElementDTO)other;
        return this.uri.equalsIgnoreCase(otherMyClass.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
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
