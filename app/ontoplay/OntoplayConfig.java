package ontoplay;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import ontoplay.models.ontologyReading.jena.FolderMapping;
import play.Environment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Created by michal on 09.02.2017.
 */
public class OntoplayConfig extends Observable{
    private final Path configFilePath;
    private Config configuration;

    public OntoplayConfig(Config configuration) {
        this.configuration = configuration;
        try {
            this.configFilePath = Paths.get(configuration.origin().url().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration origin is not an URI (??!)", e);
        }
    }

    public String getOntologyFilePath() {
        return configuration.getString("filePath");
    }

    public void setOntologyFilePath(String newFilePath) throws IOException {
        configuration = configuration.withValue("filePath", ConfigValueFactory.fromAnyRef(newFilePath));

        saveConfigToFile();
        notifyObservers();
    }


    public String getAnnotationsFilePath() {
        return configuration.getString("annotationsFilePath");
    }


    public String getCheckFilePath() {
        return configuration.getString("checkFilePath");
    }

    public List<FolderMapping> getMappings() {
        Config fileMappingsConfig = configuration.getConfig("fileMappings");
        Set<Map.Entry<String, ConfigValue>> entries = fileMappingsConfig.entrySet();
        List<FolderMapping> mappings = new ArrayList<>();

        for (Map.Entry<String, ConfigValue> entry : entries) {
            FolderMapping mapping = new FolderMapping(stripExtraQuotes(entry.getKey()), stripExtraQuotes(entry.getValue().render()));
            mappings.add(mapping);
        }

        return mappings;
    }

    private String stripExtraQuotes(String relativeFilePath){
        return  relativeFilePath.replaceAll("\"", "");
    }

    private void saveConfigToFile() throws IOException {
        String configString = configuration.root().render();
        Files.write(configFilePath, configString.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }
}
