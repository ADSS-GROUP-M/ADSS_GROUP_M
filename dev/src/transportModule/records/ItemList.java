package transportModule.records;

import com.google.gson.reflect.TypeToken;
import utils.JSON;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public record ItemList (int id, HashMap<String, Integer> load,HashMap<String, Integer> unload) {

    /**
     * @param id new id of the transport
     * @param other transport to copy from
     */
    public ItemList(int id, ItemList other){
        this(
                id,
                other.load,
                other.unload
        );
    }

    /**
     * this constructor sets the id to be -1
     */
    public ItemList(HashMap<String, Integer> load,HashMap<String, Integer> unload) {
        this(
                -1,
                load,
                unload
        );
    }

    public static ItemList getLookupObject(int id){
        return new ItemList(id,null,null);
    }

    public String toJson(){
        return JSON.serialize(this);
    }

    public static ItemList fromJson(String json){
        return JSON.deserialize(json, ItemList.class);
    }

    public static LinkedList<ItemList> listFromJson(String json){
        Type type = new TypeToken<LinkedList<ItemList>>(){}.getType();
        return JSON.deserialize(json, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemList itemList = (ItemList) o;
        return id == itemList.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
