package ontoplay.models;

import com.google.gson.*;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;

import java.lang.reflect.Type;

public class ConditionDeserializer implements
        JsonDeserializer<PropertyCondition> {

    private final PropertyProvider propertyProvider;

    public ConditionDeserializer(PropertyProvider propertyProvider) {
        this.propertyProvider = propertyProvider;
    }

    public static ClassCondition deserializeCondition(PropertyProvider jena, String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(PropertyCondition.class, new ConditionDeserializer(jena)).create();
        ClassCondition condition = gson.fromJson(json, ClassCondition.class);
        return condition;
    }

    @Override
    public PropertyCondition deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {
        System.out.println(json);
        if (json.isJsonObject()) {
            JsonObject jo = json.getAsJsonObject();

            if (jo.has("type")) {
                String type = jo.get("type").getAsString();

                switch (jo.get("type").getAsString()) {
                    case "intersection":
                        return deserializeGroupCondition(context, json, PropertyConditionType.INTERSECTION);
                    case "union":
                        return deserializeGroupCondition(context, json, PropertyConditionType.UNION);
                    case "values":
                        return deserializeGroupCondition(context, json, PropertyConditionType.VALUES);
                    case "condition":
                        PropertyValueCondition condition = null;
                        if (jo.has("individualValue")) {
                            condition = context.deserialize(json, IndividualValueCondition.class);
                        } else if (jo.has("datatypeValue")) {
                            condition = context.deserialize(json, DatatypePropertyCondition.class);
                        } else if (jo.has("classConstraintValue")) {
                            condition = context.deserialize(json, ClassValueCondition.class);
                        }

                        if (condition != null)
                            fillConditionProperty(condition);

                        return condition;
                }
            }
        }
        return null;
    }

    private PropertyGroupCondition deserializeGroupCondition(JsonDeserializationContext context, JsonElement json, PropertyConditionType type){
        PropertyGroupCondition cond = context.deserialize(json, PropertyGroupCondition.class);
        cond.setType(type);

        return cond;
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
