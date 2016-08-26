package controllers.samples;

import play.mvc.Controller;
import play.mvc.Result;


//TODO: This should generate individual describing the configuration, instead of constraints
public class Main extends Controller {
	
	public static Result index() {
			return ok( views.html.tan.startView.render("Welcome - TAN"));
	}
}