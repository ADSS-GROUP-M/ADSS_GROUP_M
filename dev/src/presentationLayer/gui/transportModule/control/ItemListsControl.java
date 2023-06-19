package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.ItemList;
import domainObjects.transportModule.Truck;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.transportModule.model.ObservableItemList;
import presentationLayer.gui.transportModule.model.ObservableTruck;
import serviceLayer.transportModule.ItemListsService;
import serviceLayer.transportModule.ResourceManagementService;
import utils.Response;

import java.util.List;

public class ItemListsControl extends AbstractControl {
    private final ItemListsService rms;

    public ItemListsControl(ItemListsService rms) {

        this.rms = rms;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {
        ObservableItemList itemList = (ObservableItemList) model;
        ItemList toAdd =  new ItemList(itemList.id, itemList.load, itemList.unload);
        String json = rms.addItemList(toAdd.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        itemList.response = response.message();
        itemList.notifyObservers();
    }

    @Override
    public void remove(ObservableUIElement observable, ObservableModel model) {
        ObservableItemList itemList = (ObservableItemList) model;
        ItemList toRemove = ItemList.getLookupObject(itemList.id);
        String json = rms.removeItemList(toRemove.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        itemList.response = response.message();
        itemList.notifyObservers();
    }

    @Override
    public void update(ObservableUIElement observable, ObservableModel model) {
        ObservableItemList itemList = (ObservableItemList) model;
        ItemList toUpdate =  new ItemList(itemList.id, itemList.load, itemList.unload);
        String json = rms.updateItemList(toUpdate.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        itemList.response = response.message();
        itemList.notifyObservers();
    }

    @Override
    public void get(ObservableUIElement observable, ObservableModel model) {
        ObservableItemList itemList = (ObservableItemList) model;
        ItemList toGet = ItemList.getLookupObject(itemList.id);
        String json = rms.getItemList(toGet.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        itemList.response = response.message();
        itemList.notifyObservers();
    }

    @Override
    public void getAll(ObservableUIElement observable, ObservableList<ObservableModel> models) {
        String json = rms.getAllItemLists();
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        List<ItemList> itemListList = ItemList.listFromJson(response.data());

        for (ItemList itemList : itemListList) {
            ObservableItemList observableItemList = new ObservableItemList();
            observableItemList.id = itemList.id();
            observableItemList.load = itemList.load();
            observableItemList.unload = itemList.unload();
            models.add(observableItemList);
        }
        models.notifyObservers();
    }

    @Override
    public ObservableModel getModel(ObservableModel lookupObject) {
        return null;
    }

    @Override
    public ObservableModel getEmptyModel() {
        return new ObservableItemList();
    }

    @Override
    public List<ObservableModel> getAllModels() {
        return null;
    }
}
