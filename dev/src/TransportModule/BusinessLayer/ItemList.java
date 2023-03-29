package TransportModule.BusinessLayer;

import java.util.HashMap;
import java.util.Objects;

public class ItemList {

    private int id;
    private HashMap<String, Integer> loadingItems;
    private HashMap<String, Integer> unloadingItems;

    public ItemList(int id, HashMap<String, Integer> load,HashMap<String, Integer> unload){
        this.id = id;
        this.loadingItems = load;
        this.unloadingItems = unload;
    }

    public static ItemList parse(int i, String json){
        return null;
    }

    public int getId() {
        return id;
    }

    public HashMap<String, Integer> getLoadingItems() {
        return loadingItems;
    }

    public HashMap<String, Integer> getUnloadingItems() {
        return unloadingItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemList itemList = (ItemList) o;
        return id == itemList.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
