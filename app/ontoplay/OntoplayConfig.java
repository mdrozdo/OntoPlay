package ontoplay;

import ontoplay.models.ontologyReading.jena.FolderMapping;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

/**
 * Created by drozd on 14.06.2017.
 */
public interface OntoplayConfig {
    String getOntologyFilePath();

    void updateOntologyConfig(String newFileName, String newUri) throws IOException;

    String getOntologyNamespace();

    String getAnnotationsFilePath();

    String getOriginalAnnotationsFilePath();

    String getCheckFilePath();

    String getUploadsPath();

    List<FolderMapping> getMappings();

    String getOntologyFileName();

    void addObserver(Observer observer);

    boolean getIgnorePropertiesWithNoDomain();
}
