package ontoplay.models.owlGeneration;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import ontoplay.controllers.PropertyConditionRenderer;
import ontoplay.models.ontologyModel.OntoProperty;

import javax.inject.Inject;
import java.util.HashMap;

/**
 * Created by michal on 02.12.2016.
 */
public class PropertyConditionRendererProvider {

    private Injector injector;

    @Inject
    public PropertyConditionRendererProvider(Injector injector){
        this.injector = injector;
    }

    public <U extends OntoProperty> PropertyConditionRenderer<U> getRenderer(U property) {
        return injector.getInstance(Key.get(new TypeLiteral<PropertyConditionRenderer<U>>(){}));
    }
}
