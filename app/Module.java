import com.google.inject.AbstractModule;
import ontoplay.controllers.MainTemplate;
import ontoplay.controllers.OntoPlayMainTemplate;
import play.Configuration;
import play.Environment;

/**
 * Created by michal on 22.11.2016.
 */
public class Module extends AbstractModule {

    private final Environment environment;
    private final Configuration configuration;
    public Module(Environment environment, Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        install(new ontoplay.Module(environment, configuration));
        bind(MainTemplate.class).to(OntoPlayMainTemplate.class);
    }

}
