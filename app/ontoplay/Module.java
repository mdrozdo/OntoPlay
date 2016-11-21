package ontoplay;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import play.Configuration;
import play.Environment;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by michal on 22.11.2016.
 */
public class Module extends AbstractModule{

    private Map<String, String> ontoplayProperties;

    public Module(Environment environment, Configuration configuration){
        ontoplayProperties = configuration.subKeys().stream()
                .filter(k->k.startsWith("ontoplay"))
                .collect(Collectors.toMap(k->k, k-> configuration.getString(k)));
    }

    @Override
    protected void configure() {
        Names.bindProperties(binder(), ontoplayProperties);

        bind(OntologyReader.class).to(JenaOwlReader.class).in(Singleton.class);
    }

}
