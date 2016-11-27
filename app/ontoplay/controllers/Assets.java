package ontoplay.controllers;

import com.google.inject.Inject;
import play.api.mvc.Action;
import play.api.mvc.AnyContent;

public class Assets {
    
    @Inject
    private static controllers.Assets assets;
    
    public static Action<AnyContent> at(String path, String file) {
        return assets.at(path, file, false);
    }
}