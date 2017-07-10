package ontoplay.models;

import com.google.gson.*;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;

import java.lang.reflect.Type;

public class ConditionDeserializer implements
        JsonDeserializer<PropertyValueCondition> {

    private PropertyProvider propertyProvider;

    public ConditionDeserializer(PropertyProvider propertyProvider) {
        this.propertyProvider = propertyProvider;
    }

    public static ClassCondition deserializeCondition(PropertyProvider jena, String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(PropertyValueCondition.class, new ConditionDeserializer(jena)).create();
        ClassCondition condition = gson.fromJson(json, ClassCondition.class);
        return condition;
    }

    @Override
    public PropertyValueCondition deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context) throws JsonParseException {
        System.out.println(json);
        PropertyValueCondition condition = null;
        if (json.isJsonObject()) {
            JsonObject jo = json.getAsJsonObject();

            //TODO: extract it away to deserializers for each property value subclass
            if (jo.has("individualValue")) {
                condition = context.deserialize(json, IndividualValueCondition.class);
            } else if (jo.has("datatypeValue")) {
                condition = context.deserialize(json, DatatypePropertyCondition.class);
            } else if (jo.has("classConstraintValue")) {
                condition = context.deserialize(json, ClassValueCondition.class);
            }
        }
        if (condition != null)
            fillConditionProperty(condition);
        return condition;
    }

    private void fillConditionProperty(PropertyValueCondition condition) {
        try {
            condition.setProperty(propertyProvider.getProperty(condition.getPropertyUri()));
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
