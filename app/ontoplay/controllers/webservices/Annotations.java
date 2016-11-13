package ontoplay.controllers.webservices;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.GsonBuilder;

import ontoplay.OntologyHelper;
import ontoplay.controllers.OntologyController;
import ontoplay.models.angular.AnnotationDTO;
import play.mvc.Result;
/**
 * 
 * @author Motasem Alwazir
 *
 */
public class Annotations extends OntologyController{
	
	
	public static Result getAnnotationPropertyByUri(String componentUri){
		try {
			componentUri= java.net.URLDecoder.decode(componentUri, "UTF-8");
			if(componentUri.indexOf(OntologyHelper.nameSpace)==-1)
				componentUri=OntologyHelper.nameSpace+componentUri;
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Error decoding annotaitons from xml " +e1.toString());
		}
		List<AnnotationDTO> annotations=new ArrayList<AnnotationDTO>();

		// add annotations from the xml
		try{
			List<AnnotationDTO> xmlAnnotations=ontoplay.controllers.configuration.AnnotationController.getAnnotationsByComponentUri(componentUri);
			for(AnnotationDTO temp:xmlAnnotations){
				annotations.add(temp);
			}
		}	catch(Exception e){
			System.out.println("Error getting annotaitons from xml " +e.toString());
		}
		
		Set<AnnotationDTO> defaultAnnotations=ontologyReader.getAnnotations(false);
		for(AnnotationDTO temp:defaultAnnotations){
			temp.setDefault(true);
			annotations.add(temp);
		}
		
		return ok(new GsonBuilder().create().toJson(annotations));
		
	}
}
