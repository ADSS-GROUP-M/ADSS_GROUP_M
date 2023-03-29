package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.BusinessFactory;
import TransportModule.BusinessLayer.ItemList;
import TransportModule.BusinessLayer.ItemListsController;

public class ItemListsService {
    private ItemListsController itemListsController;

    public ItemListsService(){
        itemListsController = BusinessFactory.getInstance().getItemListsController();
    }

    public String addItemList(String json){
        ItemList itemList = JSON.deserialize(json, ItemList.class);
        try{
            itemListsController.addItemList(itemList);
        }catch(Exception e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Item list added successfully", true).getJson();
    }

    public String removeItemList(String json){
        ItemList itemList = JSON.deserialize(json, ItemList.class);
        try{
            itemListsController.removeItemList(itemList.getId());
        }catch(Exception e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Item list removed successfully", true).getJson();
    }

    public String updateItemList(String json){
        ItemList itemList = JSON.deserialize(json, ItemList.class);
        try{
            itemListsController.updateItemList(itemList.getId(), itemList);
        }catch(Exception e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Item list updated successfully", true).getJson();
    }

    public String getItemList(String json){
        ItemList itemList = JSON.deserialize(json, ItemList.class);
        try{
            itemList = itemListsController.getItemList(itemList.getId());
        }catch(Exception e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Item list found successfully", true, itemList).getJson();
    }

    public String getAllItemLists(){
        return new Response("Item lists found successfully", true, itemListsController.getAllItemLists()).getJson();
    }

}
