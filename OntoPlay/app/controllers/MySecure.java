package controllers;

import controllers.Secure;
import controllers.Secure.Security;
import play.mvc.Before;

public class MySecure extends Secure {
	@Before(unless={"login", "authenticate", "logout"})
    static void setConnectedUser() throws Throwable {
        if(session.contains("username")) {
        	 renderArgs.put("user", Security.connected());
        }
    }	
}
