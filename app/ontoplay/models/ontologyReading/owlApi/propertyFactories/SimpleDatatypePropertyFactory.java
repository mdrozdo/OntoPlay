package ontoplay.models.ontologyReading.owlApi.propertyFactories;

import ontoplay.models.ontologyReading.owlApi.OwlPropertyFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class SimpleDatatypePropertyFactory extends OwlPropertyFactory {

    private final String[] rangeUris;

    public SimpleDatatypePropertyFactory(String... rangeUris) {
        super();
        this.rangeUris = rangeUris;
    }

    @Override
    public boolean canCreateProperty(OWLOntology onto, OWLProperty property) {
        if (!property.isOWLDataProperty())
            return false;
        OWLDataProperty dataProperty = property.asOWLDataProperty();

        Set<OWLDataRange> ranges = EntitySearcher.getRanges(dataProperty, onto.getImportsClosure().stream()).collect(Collectors.toSet());
        if (ranges.size() > 1) {
            return false;
        }

        for (OWLPropertyRange range : ranges) {
            Set<OWLDatatype> rangeDatatypes = range.getDatatypesInSignature();
            if (rangeDatatypes.size() > 1)
                return false;

            for (OWLDatatype datatype : rangeDatatypes) {
                String rangeUri = datatype.getIRI().toString();
                for (int i = 0; i < rangeUris.length; i++) {
                    if (rangeUris[i].equalsIgnoreCase(rangeUri)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}