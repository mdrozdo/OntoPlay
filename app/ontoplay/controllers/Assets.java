package ontoplay.controllers;

import com.google.inject.Inject;
import play.Configuration;
import play.api.mvc.Action;
import play.api.mvc.AnyContent;

public class Assets {

    private final Boolean sampleModeAssetRouting;
    private final controllers.Assets assets;

    @Inject
    public Assets(controllers.Assets assets, Configuration configuration) {
        this.assets = assets;
        this.sampleModeAssetRouting = configuration.getBoolean("sampleModeAssetRouting", false);
    }

    public Action<AnyContent> at(String path, String file) {
        if(sampleModeAssetRouting)
            path = "/public/";
        return assets.at(path, file, false);
    }
}