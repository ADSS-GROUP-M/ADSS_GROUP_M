package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Order;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.*;

public class OrderHistoryDataMapper extends AbstractDataMapper {
    private NumberOfOrdersCounterDataMapper numberOfOrdersCounterDataMapper;
    private Map<String, List<Order>> suppliersOrderHistory;
    public OrderHistoryDataMapper() {
        super("orders_history", new String[]{"bn_number", "order_id", "catalog_number", "quantity"});
        numberOfOrdersCounterDataMapper = new NumberOfOrdersCounterDataMapper();
        suppliersOrderHistory = new HashMap<>();
    }
    private void insert(String bnNumber, String catalog_number, int quantity, int orderId) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %d, %s, %d)",tableName, columnsString, bnNumber,
                orderId, catalog_number, quantity));
    }

    public void insert(String bnNumber, Order order) throws SQLException {
        int orderId = numberOfOrdersCounterDataMapper.getAndIncrement(bnNumber);
        Map<String, Integer> products = order.getProducts();
        for(Map.Entry<String, Integer> productOrder : products.entrySet())
            insert(bnNumber, productOrder.getKey(), productOrder.getValue(), orderId);
        if(orderId == 0)
            suppliersOrderHistory.put(bnNumber, new LinkedList<>());
        else if(!suppliersOrderHistory.containsKey(bnNumber))
            suppliersOrderHistory.put(bnNumber, findAll(bnNumber, orderId));
        suppliersOrderHistory.get(bnNumber).add(order);
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
    }

    public Order find(String bnNumber, int orderId) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = %s and order_id = %d", columnsString,
                tableName, bnNumber, orderId));
        Order order = new Order();
        while (resultSet.next()){
            order.addProduct(resultSet.getString("catalog_number"), resultSet.getInt("quantity"));
        }
        return order;
    }

    public List<Order> findAll(String bnNumber, int numberOfOrders) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = %s", columnsString,
                tableName, bnNumber));
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < numberOfOrders; i++)
            orderList.add(new Order());
        while (resultSet.next()){
            orderList.get(resultSet.getInt("order_id")).addProduct(resultSet.getString("catalog_number"), resultSet.getInt("quantity"));
        }
        return null;
    }

    public List<Order> getOrderHistory(String bnNumber) throws SQLException {
        if(suppliersOrderHistory.containsKey(bnNumber))
            return new LinkedList<>(suppliersOrderHistory.get(bnNumber));
        int numberOfOrders = numberOfOrdersCounterDataMapper.getOrderNumber(bnNumber);
        List<Order> orderHistory = findAll(bnNumber, numberOfOrders);
        suppliersOrderHistory.put(bnNumber, orderHistory);
        return new LinkedList<>(orderHistory);
    }
}
