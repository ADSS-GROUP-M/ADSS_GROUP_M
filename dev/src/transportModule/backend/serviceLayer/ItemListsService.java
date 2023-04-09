package transportModule.backend.serviceLayer;

import transportModule.backend.businessLayer.ItemListsController;
import transportModule.backend.businessLayer.records.ItemList;

public class ItemListsService {
    private final ItemListsController ilc;

    public ItemListsService(ItemListsController ilc){
        this.ilc = ilc;
    }

    public String addItemList(String json){
        ItemList itemList = JSON.deserialize(json, ItemList.class);
        try{
            ilc.addItemList(itemList);
        }catch(Exception e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Item list added successfully", true).getJson();
    }

    public String removeItemList(String json){
        ItemList itemList = JSON.deserialize(json, ItemList.class);
        try{
            ilc.removeItemList(itemList.id());
        }catch(Exception e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Item list removed successfully", true).getJson();
    }

    public String updateItemList(String json){
        ItemList itemList = JSON.deserialize(json, ItemList.class);
        try{
            ilc.updateItemList(itemList.id(), itemList);
        }catch(Exception e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Item list updated successfully", true).getJson();
    }

    public String getItemList(String json){
        ItemList itemList = JSON.deserialize(json, ItemList.class);
        try{
            itemList = ilc.getItemList(itemList.id());
        }catch(Exception e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Item list found successfully", true, itemList).getJson();
    }

    public String getAllItemLists(){
        return new Response<>("Item lists found successfully", true, ilc.getAllItemLists()).getJson();
    }

}
