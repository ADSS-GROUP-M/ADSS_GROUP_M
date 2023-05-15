package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryFixedDays;
import Backend.BusinessLayer.SuppliersModule.Order;
import Backend.BusinessLayer.SuppliersModule.PeriodicOrder;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.*;

public class PeriodicOrderDataMapper extends AbstractDataMapper {
    private PeriodicOrderDetailsDataMapper periodicOrderDetailsDataMapper;
    private Map<Integer, PeriodicOrder> periodicOrders;

    public PeriodicOrderDataMapper(){
        super("periodic_order", new String[] {"order_id", "catalog_number", "quantity"});
        periodicOrderDetailsDataMapper = new PeriodicOrderDetailsDataMapper();
        periodicOrders = new HashMap<>();
    }

    private void insert(int orderId, String catalogNumber, int quantity) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%d, '%s', %d)",tableName, columnsString,
                orderId, catalogNumber, quantity));
    }

    public void insert(String bnNumber, Order order, int day, Branch branch) throws SQLException {
        int orderId;
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT MAX(order_id) as max_id FROM %s", periodicOrderDetailsDataMapper.getTableName()));
        if(resultSet.next()){
            try {
                orderId = resultSet.getInt("max_id") + 1;
            }
            catch (Exception e){
                orderId = 0;
            }
        }
        else
            orderId = 0;
        periodicOrderDetailsDataMapper.insert(orderId, bnNumber, day, branch);
        Map<String, Integer> products = order.getProducts();
        for(Map.Entry<String, Integer> productOrder : products.entrySet())
            insert(orderId, productOrder.getKey(), productOrder.getValue());
        periodicOrders.put(orderId, new PeriodicOrder(bnNumber, order, day, branch));
    }

    public PeriodicOrder getPeriodicOrder(int orderId) throws SQLException {
        if(periodicOrders.containsKey(orderId))
            return periodicOrders.get(orderId);

        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE order_id = %d",
                columnsString, tableName, orderId));
        Order order = new Order();
        while (resultSet.next())
            order.addProduct(resultSet.getString("catalog_number"), resultSet.getInt("quantity"));
        if(!order.getProducts().isEmpty()){
            int day = periodicOrderDetailsDataMapper.findDay(orderId);
            String bnNumber = periodicOrderDetailsDataMapper.findBnNumber(orderId);
            Branch branch = periodicOrderDetailsDataMapper.findBranch(orderId);
            PeriodicOrder periodicOrder = new PeriodicOrder(bnNumber, order, day, branch);
            periodicOrders.put(orderId, periodicOrder);
            return periodicOrder;
        }
        return null;
    }

    public void update(int orderId, int day) throws SQLException {
        periodicOrderDetailsDataMapper.update(orderId, day);
    }

    public int getOrderId(PeriodicOrder periodicOrder){
        for(Map.Entry<Integer, PeriodicOrder> order : periodicOrders.entrySet())
            if(order.getValue().equals(periodicOrder))
                return order.getKey();
        return 0;
    }

    public Map<Integer, PeriodicOrder> getAllPeriodicOrders() throws SQLException {
        int maxOrderId;
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT MAX(order_id) as max_id FROM %s", periodicOrderDetailsDataMapper.getTableName()));
        maxOrderId = resultSet.next() ? resultSet.getInt("max_id") : 0;
        for(int i = 0; i <= maxOrderId; i++){
            if(!periodicOrders.containsKey(i)) {
                PeriodicOrder periodicOrder = getPeriodicOrder(i);
                if (periodicOrder != null)
                    periodicOrders.put(i, periodicOrder);
            }
        }
        return periodicOrders;
    }

    public int findDay(String catalogNumber, Branch branch) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("select day from periodic_order JOIN periodic_order_details on periodic_order.order_id = periodic_order_details.order_id where periodic_order_details.branch = '%s' and periodic_order.catalog_number = '%s'", branch.name(), catalogNumber));
        int minDay = 7;
        List<Integer> days = new LinkedList<>();
        while (resultSet.next()) {
            days.add(resultSet.getInt("day"));
        }
        return getDay(days);
    }

    private int getDay(List<Integer> days) throws SQLException {
        int dayNow = Calendar.DAY_OF_WEEK;
        if(days.contains(dayNow))
            return 0;
        //try to look for day greater than now, but in this week
        int dayChosen = dayNow;
        int maxDay = 7;
        for(int day : days)
            if(day > dayNow && day < maxDay) {
                dayChosen = day;
                maxDay = day;
            }
        if(dayChosen != dayNow)
            return dayChosen - dayNow;
        //try to look for day smaller than now, that in next week
        for(int day : days)
            if(day < dayChosen)
                dayChosen = day;
        return (dayChosen + 7) - dayNow;
    }

}
