package ontoplay.controllers;

import play.twirl.api.Html;
import scala.Function1;

import static scala.compat.java8.JFunction.func;

/**
 * Created by drozd on 16.06.2017.
 */
public class OntoPlayMainTemplate implements MainTemplate {

    public Function1<String, Function1<Html, Function1<Html, Html>>> getRenderFunction() {
        return func((String title) ->
                func((Html otherHeader) ->
                        func((Html content) ->
                                ontoplay.views.html.ontoplayMain.render(title, otherHeader, content))));
    }
}
