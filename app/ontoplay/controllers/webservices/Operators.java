package ontoplay.controllers.webservices;

import java.io.UnsupportedEncodingException;

import com.google.gson.GsonBuilder;

import ontoplay.OntologyHelper;
import ontoplay.controllers.OntologyController;
import ontoplay.controllers.PropertyConditionRenderer;
import ontoplay.models.ConfigurationException;
import ontoplay.models.angular.OperatorDTO;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.owlGeneration.PropertyConditionRendererProvider;
import play.Logger.ALogger;
import play.mvc.Result;

import javax.inject.Inject;

public class Operators extends OntologyController{

	private final PropertyConditionRendererProvider conditionRendererProvider;

	@Inject
	public Operators(OntologyHelper ontoHelper, PropertyConditionRendererProvider conditionRendererProvider) {
		super(ontoHelper);
		this.conditionRendererProvider = conditionRendererProvider;
	}

		public Result getOpertors(String propertyUri,boolean isDescriptionOfIndividual){
			try{
				propertyUri= java.net.URLDecoder.decode(propertyUri, "UTF-8");
				ALogger log=play.Logger.of("application");
			OntoProperty property = ontoHelper.getProperty(propertyUri);
			log.info("Getting opertor for "+propertyUri+" of the class "+property.getClass());
		
			
			PropertyConditionRenderer conditionRenderer = conditionRendererProvider
					.getRenderer(property);
			
			OperatorDTO operatorDTO=new OperatorDTO(getInputType(property.getClass()),
					conditionRenderer.getOperators(isDescriptionOfIndividual));
			return ok(new GsonBuilder().create().toJson(operatorDTO));
			
			}catch(ConfigurationException e){
				return badRequest();
			} catch (UnsupportedEncodingException e) {
				return badRequest();
			}
		}
		
		private static String getInputType(Class<? extends OntoProperty> propertyCLass){
			String classAsString=(propertyCLass+"").toLowerCase();
			if(classAsString.indexOf("object")>-1)
				return "object";
			else if(classAsString.indexOf("float") > -1 || classAsString.indexOf("integer") > -1)
				return "number";
			else if(classAsString.indexOf("date") > -1)
				return "date";
			else
				return "text";
		}
}
