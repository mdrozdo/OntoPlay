package ontoplay;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import ontoplay.models.ontologyReading.jena.FolderMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Created by michal on 09.02.2017.
 */
public class FileOntoplayConfig extends Observable implements OntoplayConfig {
    private final Path configFilePath;
    private Config configuration;

    public FileOntoplayConfig(File ontoPlayConfigFile) {

        this.configuration = ConfigFactory.parseFile(ontoPlayConfigFile);
        this.configFilePath = ontoPlayConfigFile.toPath();

    }

    @Override
    public String getOntologyFilePath() {
        return configuration.getString("filePath");
    }

    @Override
    public void updateOntologyConfig(String newFileName, String newUri) throws IOException {
        String newFilePath = Paths.get(getOntologyFilePath()).getParent().resolve(newFileName).toString();

        configuration = configuration.withValue("filePath", ConfigValueFactory.fromAnyRef(newFilePath));
        configuration = configuration.withValue("ontologyNamespace", ConfigValueFactory.fromAnyRef(newUri));

        saveConfigToFile();
        notifyObservers();
    }

    @Override
    public String getOntologyNamespace() {
        return configuration.getString("ontologyNamespace");
    }

    @Override
    public String getAnnotationsFilePath() {
        return configuration.getString("annotationsFilePath");
    }

    @Override
    public String getOriginalAnnotationsFilePath() {
        return configuration.getString("originalAnnotationsFilePath");
    }

    @Override
    public String getCheckFilePath() {
        return configuration.getString("checkFilePath");
    }

    @Override
    public String getUploadsPath() {
        return configuration.getString("uploadsPath");
    }

    @Override
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

    private String stripExtraQuotes(String relativeFilePath) {
        return relativeFilePath.replaceAll("\"", "");
    }

    private void saveConfigToFile() throws IOException {
        String configString = configuration.root().render();
        Files.write(configFilePath, configString.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }


    @Override
    public String getOntologyFileName() {
        String filePath = getOntologyFilePath();
        return Paths.get(filePath).getFileName().toString();
    }

}
