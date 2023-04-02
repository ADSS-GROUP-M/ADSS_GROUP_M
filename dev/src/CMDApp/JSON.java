package CMDApp;

import CMDApp.Records.Driver;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class JSON {

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime>{
        @Override
        public JsonElement serialize(LocalDateTime src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    }

    private static Gson gson =  new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class,new LocalDateTimeAdapter())
            .enableComplexMapKeySerialization()
            .create();

    public static <T> String serialize(T obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserialize(String json, Type TypeOfT) {
        return gson.fromJson(json, TypeOfT);
    }
}

