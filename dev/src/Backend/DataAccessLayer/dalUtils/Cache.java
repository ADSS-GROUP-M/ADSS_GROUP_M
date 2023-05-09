package Backend.DataAccessLayer.dalUtils;

import java.util.Collection;
import java.util.HashMap;

public class Cache <T> {
    private final HashMap<Integer,T> map;

    public Cache() {
        map = new HashMap<>();
    }

    public void put(T object){
        map.put(object.hashCode(), object);
    }

    public void putAll(Collection<T> objects){
        for (T object : objects) {
            put(object);
        }
    }

    public T get(T object){
        return map.get(object.hashCode());
    }

    public void remove(T object){
        map.remove(object.hashCode());
    }

    public boolean contains(T object){
        return map.containsKey(object.hashCode());
    }

    public void clear(){
        map.clear();
    }
}
