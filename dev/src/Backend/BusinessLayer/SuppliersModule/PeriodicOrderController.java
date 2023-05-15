package Backend.BusinessLayer.SuppliersModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.DataAccessLayer.SuppliersModule.PeriodicOrderDataMapper;

import java.sql.SQLException;
import java.util.Map;

public class PeriodicOrderController {
    private static PeriodicOrderController instance;

    private PeriodicOrderDataMapper periodicOrderDataMapper;

    private PeriodicOrderController(){
        periodicOrderDataMapper = new PeriodicOrderDataMapper();
    }

    public static PeriodicOrderController getInstance(){
        if(instance == null)
            instance = new PeriodicOrderController();
        return instance;
    }
    public PeriodicOrder getPeriodicOrder(int orderId) throws SQLException {
        return periodicOrderDataMapper.getPeriodicOrder(orderId);
    }
    public void addPeriodicOrder(String bnNumber, Map<String, Integer> products, int day, Branch branch) throws SQLException {
        periodicOrderDataMapper.insert(bnNumber, new Order(products), day, branch);
    }

    public void setDay(int orderId, int day) throws SQLException {
        periodicOrderDataMapper.update(orderId, day);
    }

    public String getDetails(int orderId) throws SQLException {
        return getPeriodicOrder(orderId).toString(orderId);
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
        int minDay = periodicOrderDataMapper.findDay(catalogNumber, Branch.branch4);
        return minDay == 7 ? -1 : minDay;
    }



}
