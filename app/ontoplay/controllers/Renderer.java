package ontoplay.controllers;

import java.util.Map;

public interface Renderer {
    public void renderTemplate(String templateName, Map<String, Object> args);
}
