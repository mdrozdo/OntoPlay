package ontoplay.controllers.webservices;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.GsonBuilder;

import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.controllers.OntologyController;
import ontoplay.controllers.configuration.utils.OntoplayAnnotationUtils;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.angular.AnnotationDTO;
import ontoplay.models.ontologyReading.OntologyReader;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * 
 * @author Motasem Alwazir
 *
 */
public class Annotations extends OntologyController{

	private OntologyReader ontologyReader;
	private OntoplayAnnotationUtils ontoplayAnnotationUtils;

	@Inject
	public Annotations(OntologyUtils ontoHelper, OntologyReader ontologyReader, OntoplayAnnotationUtils ontoplayAnnotationUtils) {
		super(ontoHelper);
		this.ontologyReader = ontologyReader;
		this.ontoplayAnnotationUtils = ontoplayAnnotationUtils;
	}
	
	public Result getAnnotationPropertyByUri(String componentUri){
		try {
			componentUri = java.net.URLDecoder.decode(componentUri, "UTF-8");
			String namespace = ontologyReader.getOntologyNamespace();
			if (componentUri.indexOf(namespace) == -1)
				componentUri = namespace + componentUri;
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Error decoding annotaitons from xml " + e1.toString());
		}
		List<AnnotationDTO> annotations=new ArrayList<AnnotationDTO>();

		// add annotations from the xml
		try {
			List<AnnotationDTO> xmlAnnotations = ontoplayAnnotationUtils.getAnnotationForComponentByComponentUri(componentUri);

			for (AnnotationDTO temp : xmlAnnotations) {
				annotations.add(temp);
			}
		}	catch(Exception e) {
			System.out.println("Error getting annotaitons from xml " + e.toString());
		}
		
		Set<AnnotationDTO> defaultAnnotations=ontologyReader.getAnnotations(false);
		for(AnnotationDTO temp:defaultAnnotations){
			temp.setDefault(true);
			annotations.add(temp);
		}
		
		return ok(new GsonBuilder().create().toJson(annotations));
		
	}
}
