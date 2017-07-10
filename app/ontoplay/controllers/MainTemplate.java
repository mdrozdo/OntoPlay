package ontoplay.controllers;

import play.twirl.api.Html;
import scala.Function1;

/**
 * Created by drozd on 16.06.2017.
 */
public interface MainTemplate {

    Function1<String, Function1<Html, Function1<Html, Html>>> getRenderFunction();
}
