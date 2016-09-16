package ontoplay.models.ontologyReading.owlApi.propertyFactories;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyRange;

import ontoplay.models.ontologyReading.owlApi.OwlPropertyFactory;

public abstract class SimpleDatatypePropertyFactory extends OwlPropertyFactory {

	private final String[] rangeUris;
	
	public SimpleDatatypePropertyFactory(String... rangeUris) {
		super();
		this.rangeUris = rangeUris;
	}

	@Override
	public boolean canCreateProperty(OWLOntology onto, OWLProperty property) {
		if(!property.isOWLDataProperty())
			return false;
		OWLDataProperty dataProperty = property.asOWLDataProperty();
		
		Set<OWLDataRange> ranges = dataProperty.getRanges(onto.getImports());
		if(ranges.size() > 1){
			return false;
		}		
		
		for (OWLPropertyRange range : ranges) {
			Set<OWLDatatype> rangeDatatypes = range.getDatatypesInSignature();
			if(rangeDatatypes.size() > 1)
				return false;
			
			for (OWLDatatype datatype : rangeDatatypes) {
				String rangeUri = datatype.getIRI().toString();
				for (int i = 0; i < rangeUris.length; i++) {
					if(rangeUris[i].equalsIgnoreCase(rangeUri)){
						return true;
					}				
				}								
			}
		}		
		return false;
	}
}