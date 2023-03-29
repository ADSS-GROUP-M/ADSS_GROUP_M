package TransportModule.ServiceLayer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JsonSerializer {

    private static Gson gson =  new GsonBuilder().setPrettyPrinting().create();

    public static <T> String serialize(T obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserialize(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}

