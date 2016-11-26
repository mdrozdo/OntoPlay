package ontoplay.models.ontologyReading.jena;

/**
 * Created by michal on 26.11.2016.
 */
public class FolderMapping {
    private String uri;
    private String folderPath;

    public FolderMapping(String uri, String folderPath) {
        this.uri = uri;
        this.folderPath = folderPath;
    }

    public String getUri() {
        return uri;
    }

    public String getFolderPath() {
        return folderPath;
    }
}
