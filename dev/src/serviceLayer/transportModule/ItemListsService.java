package serviceLayer.transportModule;

import businessLayer.transportModule.ItemListsController;
import exceptions.TransportException;
import objects.transportObjects.ItemList;
import utils.Response;

public class ItemListsService {
    private final ItemListsController ilc;

    public ItemListsService(ItemListsController ilc){
        this.ilc = ilc;
    }

    /**
     * @param json serialized ItemList with id -1
     * @return serialized {@link Response} object with the id of the added item list in the data field
     * @throws UnsupportedOperationException if itemList.id() != -1
     */
    public String addItemList(String json){
        ItemList itemList = ItemList.fromJson(json);
        Integer id = null;
        ItemList added;
        try {
              added = ilc.addItemList(itemList);
        } catch (TransportException e) {
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Item list added successfully with id"+id, true, added).toJson();
    }

    /**
     * @param json a serialized {@link ItemList#getLookupObject(int)}
     */
    public String removeItemList(String json){
        ItemList itemList = ItemList.fromJson(json);
        try{
            ilc.removeItemList(itemList.id());
            return new Response("Item list removed successfully", true).toJson();
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String updateItemList(String json){
        ItemList itemList = ItemList.fromJson(json);
        ItemList updated;
        try{
           updated= ilc.updateItemList(itemList.id(), itemList);
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Item list updated successfully", true,updated).toJson();
    }

    /**
     * @param json a serialized {@link ItemList#getLookupObject(int)}
     */
    public String getItemList(String json){
        ItemList itemList = ItemList.fromJson(json);
        try{
            itemList = ilc.getItemList(itemList.id());
            return new Response("Item list found successfully", true, itemList).toJson();
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getAllItemLists(){
        try {
            return new Response("Item lists found successfully", true, ilc.getAllItemLists()).toJson();
        } catch (TransportException e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

}
