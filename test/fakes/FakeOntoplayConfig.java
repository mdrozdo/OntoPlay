package fakes;

import ontoplay.OntoplayConfig;
import ontoplay.models.ontologyReading.jena.FolderMapping;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

/**
 * Created by drozd on 14.06.2017.
 */
public class FakeOntoplayConfig implements OntoplayConfig {
    @Override
    public String getOntologyFilePath() {
        return null;
    }

    @Override
    public void updateOntologyConfig(String newFileName, String newUri) throws IOException {

    }

    @Override
    public String getOntologyNamespace() {
        return null;
    }

    @Override
    public String getAnnotationsFilePath() {
        return null;
    }

    @Override
    public String getOriginalAnnotationsFilePath() {
        return null;
    }

    @Override
    public String getCheckFilePath() {
        return null;
    }

    @Override
    public String getUploadsPath() {
        return null;
    }

    @Override
    public List<FolderMapping> getMappings() {
        return null;
    }

    @Override
    public String getOntologyFileName() {
        return null;
    }

    @Override
    public void addObserver(Observer observer) {

    }
}
