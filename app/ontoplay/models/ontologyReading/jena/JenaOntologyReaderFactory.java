package ontoplay.models.ontologyReading.jena;

import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.OntologyReaderFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by drozd on 15.06.2017.
 */
public class JenaOntologyReaderFactory implements OntologyReaderFactory {
    private OwlPropertyFactory owlPropertyFactory;

    @Inject
    public JenaOntologyReaderFactory(OwlPropertyFactory owlPropertyFactory){

        this.owlPropertyFactory = owlPropertyFactory;
    }

    @Override
    public OntologyReader create(String filePath, String ontologyNamespace, List<FolderMapping> localMappings, boolean ignorePropsWithNoDomain) {
        return new JenaOwlReader(owlPropertyFactory, filePath, ontologyNamespace, localMappings, ignorePropsWithNoDomain);
    }
}
