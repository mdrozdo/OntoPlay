package ontoplay.models.ontologyReading;

import ontoplay.models.ontologyReading.jena.FolderMapping;

import java.util.List;

/**
 * Created by michal on 24.11.2016.
 */
public interface OntologyReaderFactory {
    OntologyReader create(String filePath, List<FolderMapping> localMappings, boolean ignorePropsWithNoDomain);
}
