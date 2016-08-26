import java.lang.reflect.Method;

import jobs.PropertyTypeConfiguration;

import play.*;
import play.mvc.Action;
import play.mvc.Http.Request;

public class Global extends GlobalSettings{
    @Override
    public void onStart(Application app)
    {
        Logger.info("Application has started");
    }
    
    @Override
    public Action onRequest(Request arg0, Method arg1) {
    	try {
			new PropertyTypeConfiguration().doJob();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return super.onRequest(arg0, arg1);
    }
}