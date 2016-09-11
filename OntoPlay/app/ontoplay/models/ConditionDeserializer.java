package ontoplay.models;

import java.lang.reflect.Type;

import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ConditionDeserializer implements
		JsonDeserializer<PropertyValueCondition> {
	
	public static ClassCondition deserializeCondition(PropertyProvider jena, String json){
		Gson gson = new GsonBuilder().registerTypeAdapter(PropertyValueCondition.class, new ConditionDeserializer(jena)).create();
		ClassCondition condition = gson.fromJson(json, ClassCondition.class);
		return condition;
	}

	private PropertyProvider propertyProvider;
	
	public ConditionDeserializer(PropertyProvider propertyProvider) {
		this.propertyProvider = propertyProvider;
	}

	@Override
	public PropertyValueCondition deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		System.out.println(json);
		PropertyValueCondition condition = null;
		if(json.isJsonObject()){
			JsonObject jo = json.getAsJsonObject();
			
			//TODO: extract it away to deserializers for each property value subclass
			if(jo.has("individualValue")){
				condition = context.deserialize(json, IndividualValueCondition.class);
			}
			else if(jo.has("datatypeValue")){
				condition = context.deserialize(json, DatatypePropertyCondition.class);
			} 
			else if(jo.has("classConstraintValue")){
				condition = context.deserialize(json, ClassValueCondition.class);
			}
		}
		if(condition != null)
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
