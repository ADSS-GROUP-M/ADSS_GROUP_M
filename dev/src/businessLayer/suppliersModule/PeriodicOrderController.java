package businessLayer.suppliersModule;

import businessLayer.businessLayerUsage.Branch;
import dataAccessLayer.suppliersModule.PeriodicOrderDataMapper;
import exceptions.DalException;
import exceptions.SupplierException;

import java.sql.SQLException;
import java.util.Map;

public class PeriodicOrderController {

    private final PeriodicOrderDataMapper periodicOrderDataMapper;
    private final AgreementController agreementController;

    public PeriodicOrderController(PeriodicOrderDataMapper periodicOrderDataMapper, AgreementController agreementController){
        this.periodicOrderDataMapper = periodicOrderDataMapper;
        this.agreementController = agreementController;
    }

    public PeriodicOrder getPeriodicOrder(int orderId) throws SupplierException {
        try {
            return periodicOrderDataMapper.getPeriodicOrder(orderId);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }
    public void addPeriodicOrder(String bnNumber, Map<String, Integer> products, int day, Branch branch) throws SupplierException {
        for(Map.Entry<String, Integer> productOrder : products.entrySet()){
            String catalogNumber = productOrder.getKey();
            int quantity = productOrder.getValue();
            if(agreementController.getProduct(bnNumber, catalogNumber).getNumberOfUnits() < quantity) {
                throw new SupplierException("supplier - " + bnNumber + "can't supply more than - " + agreementController.getProduct(bnNumber, catalogNumber).getNumberOfUnits() + " units of product - " + catalogNumber);
            }
        }
        try {
            periodicOrderDataMapper.insert(bnNumber, new Order(products), day, branch);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setDay(int orderId, int day) throws SupplierException {
        try {
            periodicOrderDataMapper.update(orderId, day);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public String getDetails(int orderId) throws SupplierException {
        return getPeriodicOrder(orderId).toString(orderId);
    }


    /***
     * to use after insert
     * @return the details of the order
     */
    public String getDetails() throws SupplierException {
        try {
            Map<Integer, PeriodicOrder> periodicOrders = periodicOrderDataMapper.getAllPeriodicOrders();
            StringBuilder res = new StringBuilder();
            for(Map.Entry<Integer, PeriodicOrder> periodicOrder : periodicOrders.entrySet()) {
                res.append("\n\nPERIODIC ORDER:\n").append(periodicOrder.getValue().toString(periodicOrder.getKey()));
            }
            return res.toString();
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public int getDaysForOrder(String catalogNumber, Branch branch) throws SupplierException {
        try {
            int minDay = periodicOrderDataMapper.findDay(catalogNumber, branch);
            return minDay == 7 ? -1 : minDay;
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }



}
