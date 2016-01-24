package controllers.webservices;

import java.io.UnsupportedEncodingException;

import com.google.gson.GsonBuilder;

import controllers.OntologyController;
import controllers.PropertyConditionRenderer;
import models.ConfigurationException;
import models.angular.OperatorDTO;
import models.ontologyModel.OntoProperty;
import play.Logger.ALogger;
import play.mvc.Result;

public class Operators extends OntologyController{

		public static Result getOpertors(String propertyUri){
			try{
				propertyUri= java.net.URLDecoder.decode(propertyUri, "UTF-8");
				ALogger log=play.Logger.of("application");
			OntoProperty property = ontologyReader.getProperty(propertyUri);
			log.info(propertyUri+" " +property.getClass());
			
			PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
					.getRenderer(property.getClass());
			OperatorDTO operatorDTO=new OperatorDTO(getInputType(property.getClass()),conditionRenderer.getOperators(true));
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
