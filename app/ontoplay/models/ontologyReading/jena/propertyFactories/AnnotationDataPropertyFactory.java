package ontoplay.models.ontologyReading.jena.propertyFactories;

import ontoplay.models.Constants;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.jena.OwlPropertyFactory;
import ontoplay.models.properties.AnnotationDataProperty;
import org.apache.jena.ontology.OntProperty;

public class AnnotationDataPropertyFactory extends OwlPropertyFactory {

    @Override
    public boolean canCreateProperty(OntProperty ontProperty) {

        if (!ontProperty.isAnnotationProperty())
            return false;
        if (ontProperty.getRange() == null)
            return false;
        if (ontProperty.getRange().getURI() == null)
            return false;

        String rangeUri = ontProperty.getRange().getURI();
        //get all allowed ranges for Annotation property
        StringBuilder builder = new StringBuilder(Constants.DATE_TIME_RANGES).append(Constants.FLOAT_DATA_RANGES).append(Constants.INTEGER_DATA_RANGES).append(Constants.STRING_DATA_RANGES);
        return builder.indexOf(rangeUri) > -1;
    }

    @Override
    public OntoProperty createProperty(OntProperty ontProperty) {
        return new AnnotationDataProperty(ontProperty.getNameSpace(), ontProperty.getLocalName(), Constants.translateDataType(ontProperty.getURI()),
                ontProperty.getLabel(""));
    }


}
