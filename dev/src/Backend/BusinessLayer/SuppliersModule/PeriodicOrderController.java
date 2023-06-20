package Backend.BusinessLayer.SuppliersModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.DataAccessLayer.SuppliersModule.PeriodicOrderDataMapper;

import java.sql.SQLException;
import java.util.Map;

public class PeriodicOrderController {
    private static PeriodicOrderController instance;

    private PeriodicOrderDataMapper periodicOrderDataMapper;
    private AgreementController agreementController;

    private PeriodicOrderController(){
        periodicOrderDataMapper = new PeriodicOrderDataMapper();
        agreementController = AgreementController.getInstance();
    }

    public static PeriodicOrderController getInstance(){
        if(instance == null)
            instance = new PeriodicOrderController();
        return instance;
    }
    public PeriodicOrder getPeriodicOrder(int orderId) throws SQLException {
        return periodicOrderDataMapper.getPeriodicOrder(orderId);
    }

    public void removePeriodicOrder(int orderId) throws SQLException {
        periodicOrderDataMapper.delete(orderId);
    }
    public void addPeriodicOrder(String bnNumber, Map<String, Integer> products, int day, Branch branch) throws Exception {
        if(day < 0 || day > 6)
            throw new Exception("invalid day");
        for(Map.Entry<String, Integer> productOrder : products.entrySet()){
            String catalogNumber = productOrder.getKey();
            int quantity = productOrder.getValue();
            if(!agreementController.productExist(bnNumber, catalogNumber))
                throw new Exception("supplier - " + bnNumber + " doesn't supply product - " + catalogNumber);
            if(agreementController.getProduct(bnNumber, catalogNumber).getNumberOfUnits() < quantity)
                throw new Exception("supplier - " + bnNumber + " can't supply more than - " + agreementController.getProduct(bnNumber, catalogNumber).getNumberOfUnits() + " units of product - " + catalogNumber);
        }
        periodicOrderDataMapper.insert(bnNumber, new Order(products), day, branch);
    }

    public void setDay(int orderId, int day) throws SQLException {
        periodicOrderDataMapper.update(orderId, day);
    }

    public String getDetails(int orderId) throws SQLException {
        return getPeriodicOrder(orderId).toString(orderId);
    }

    public Map<Integer, PeriodicOrder> getAllPeriodicOrders() throws SQLException {
        Map<Integer, PeriodicOrder> periodicOrders = periodicOrderDataMapper.getAllPeriodicOrders();
        return periodicOrders;
    }

    /***
     * to use after insert
     * @return the details of the order
     */
    public String getDetails() throws SQLException {
        Map<Integer, PeriodicOrder> periodicOrders = periodicOrderDataMapper.getAllPeriodicOrders();
        String res = "";
        for(Map.Entry<Integer, PeriodicOrder> periodicOrder : periodicOrders.entrySet())
            res += "\n\nPERIODIC ORDER:\n" + periodicOrder.getValue().toString(periodicOrder.getKey());
        return res;
    }

    public int getDaysForOrder(String catalogNumber, Branch branch) throws SQLException {
        int minDay = periodicOrderDataMapper.findDay(catalogNumber, branch);
        return minDay == 7 ? -1 : minDay;
    }



}
