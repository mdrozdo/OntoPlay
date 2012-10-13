package controllers;

import play.*;
import play.mvc.*;

import java.io.File;

import controllers.Secure.Security;

public class Application extends Controller {

    public static void index() {
    	//String user = Security.connected();
        render();
    }
}