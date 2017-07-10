package ontoplay.controllers;

import com.google.inject.Inject;
import play.api.mvc.Action;
import play.api.mvc.AnyContent;

public class Assets {

    private controllers.Assets assets;

    @Inject
    public Assets(controllers.Assets assets) {
        this.assets = assets;
    }

    public Action<AnyContent> at(String path, String file) {

        return assets.at(path, file, false);
    }
}