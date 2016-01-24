import java.lang.reflect.Method;

import models.ontologyReading.jena.JenaOwlReaderConfig;
import controllers.org.TANHelper;
import jobs.JenaOwlReaderConfiguration;
import jobs.OntologyGeneratorConfiguration;
import jobs.PropertyTypeConfiguration;
import play.*;
import play.mvc.Action;
import play.mvc.Http.Request;

public class Global extends GlobalSettings{
    @Override
    public void onStart(Application app)
    {
    	//new JenaOwlReaderConfiguration().initialize(TANHelper.file,new JenaOwlReaderConfig().useLocalMapping(TANHelper.iriString,TANHelper.fileName));
    	try {
			new PropertyTypeConfiguration().doJob();
			//Logger.info("\n\n\n REGISTERED \n\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Logger.info("Application has started");
    }
    
    @Override
    public Action onRequest(Request arg0, Method arg1) { 
    	try {
    		new JenaOwlReaderConfiguration().initialize(TANHelper.file,new JenaOwlReaderConfig().useLocalMapping(TANHelper.iriString,TANHelper.fileName));
			new OntologyGeneratorConfiguration().doJob();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return super.onRequest(arg0, arg1);
    }
}