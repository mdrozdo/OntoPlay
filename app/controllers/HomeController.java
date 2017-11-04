package controllers;

import ontoplay.OntoplayConfig;
import ontoplay.controllers.MainTemplate;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by drozd on 04.11.2017.
 */
public class HomeController extends Controller {

    private final MainTemplate mainTemplate;

    @Inject
    public HomeController(MainTemplate mainTemplate) {

        this.mainTemplate = mainTemplate;
    }
    public Result index(){
        return ok(views.html.index.render(mainTemplate.getRenderFunction()));
    }
}
