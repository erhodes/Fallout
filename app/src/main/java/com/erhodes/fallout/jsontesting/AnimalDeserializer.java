package com.erhodes.fallout.jsontesting;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AnimalDeserializer implements JsonDeserializer<HashMap<String, Animal>> {
    @Override
    public HashMap<String, Animal> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        HashMap<String, Animal> result = new HashMap<>();
        for (Map.Entry entry : json.getAsJsonObject().entrySet()) {
            JsonElement element = (JsonElement) entry.getValue();
            if (element.getAsJsonObject().has("breed")) {
                Dog dog = context.deserialize(element, Dog.class);
                result.put("dog", dog);
            } else {
                Animal animal = context.deserialize(element, Animal.class);
                result.put(animal.name, animal);
            }
        }
        return result;
    }
}
