package Fronend.PresentationLayer.SuppliersModule.GUI.ModelLayer;

import Backend.BusinessLayer.SuppliersModule.Pair;
import Backend.BusinessLayer.SuppliersModule.Supplier;
import Backend.ServiceLayer.SuppliersModule.Response;
import Backend.ServiceLayer.SuppliersModule.SupplierService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SuppliersModel {
    private SupplierService supplierService;
    private Gson gson;
    private Map<String, Supplier> suppliers;

    public SuppliersModel(){
        supplierService = new SupplierService();
        suppliers = new HashMap<>();
        gson = new Gson();
    }

    public Supplier getSupplier(String bnNumber){
        Type responseSupplier = new TypeToken<Response<Supplier>>(){}.getType();
        Response<Supplier> response = gson.fromJson(supplierService.getSupplier(bnNumber), responseSupplier);
        Supplier s = response.getReturnValue();
        if(s != null)
            suppliers.put(bnNumber, s);
        return s;
    }

    public boolean setName(String bnNumber, String name){
        Response r = gson.fromJson(supplierService.setSupplierName(bnNumber, name), Response.class);
        return !r.errorOccurred();
    }

    public Boolean setBnNumber(String bnNumber, String value) {
        Response r = gson.fromJson(supplierService.setSupplierBnNumber(bnNumber, value), Response.class);
        return !r.errorOccurred();
    }

    public Boolean setBankAccount(String bnNumber, String value) {
        String[] s = value.split(",");
        Response r = gson.fromJson(supplierService.setSupplierBankAccount(bnNumber, s[0].strip(), s[1].strip(), s[2].strip()), Response.class);
        return !r.errorOccurred();
    }

    public Boolean setPaymentMethod(String bnNumber, String value) {
        Response r = gson.fromJson(supplierService.setSupplierPaymentMethod(bnNumber, value), Response.class);
        return !r.errorOccurred();
    }
}
