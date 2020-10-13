package ontoplay.controllers.webservices;

import com.google.gson.GsonBuilder;
import ontoplay.controllers.OntologyController;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.Constants;
import ontoplay.models.dto.OwlElementDTO;
import ontoplay.models.dto.PropertyDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Motasem Alwazir
 */

public class Properties extends OntologyController {

    @Inject
    public Properties(OntologyUtils ontoHelper) {
        super(ontoHelper);
    }

    public Result getPropertiesByClassName(String className) {
        try {
            OntoClass owlClass = ontologyUtils.getOwlClass(className);
            List<OntoProperty> properties = owlClass.getProperties();
            var propertiesDTO = properties.stream()
                    .map(p -> {
                        var domainSize = p.getDomain().size();

                        return new PropertyDTO(p.getUri(), p.getLocalName(), domainSize, p.getRelevance());
                    })
                    .sorted(Comparator.comparing(PropertyDTO::getRelevance).reversed()
                        .thenComparing(PropertyDTO::getLocalName))
                    .collect(Collectors.toList());

            propertiesDTO.add(new PropertyDTO(Constants.HAS_LOCAL_NAME_URI, Constants.HAS_LOCAL_NAME_NAME, 1, 1.0));

            return ok(new GsonBuilder().create().toJson(propertiesDTO));
        } catch (Exception e) {
            return badRequest();
        }
    }

}
