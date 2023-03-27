package BusinessLayer;

import java.util.HashMap;

public class ItemList {
    private int id;
    private HashMap<String, Integer> items;

    public ItemList(int id, HashMap<String, Integer> items){
        this.id = id;
        this.items = items;
    }

    public static ItemList parse(String json){
        return null;
    }

    public int getId() {
        return id;
    }

    public HashMap<String, Integer> getItems() {
        return items;
    }
}
