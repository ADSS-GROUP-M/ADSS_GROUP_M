package ServiceLayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer {

    private static Gson gson =  new GsonBuilder().setPrettyPrinting().create();

    public static <T> String serialize(T obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserialize(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}

