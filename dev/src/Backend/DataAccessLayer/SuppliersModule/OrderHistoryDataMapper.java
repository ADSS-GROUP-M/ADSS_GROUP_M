package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Order;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryDataMapper extends AbstractDataMapper {
    public OrderHistoryDataMapper() {
        super("Orders_history", new String[]{"bn_number", "order_id", "catalog_number", "quantity"});
    }
    public void insert(String bnNumber, int orderId, String catalog_number, int quantity) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %d, %s, %d)",tableName, columnsString, bnNumber,
                orderId, catalog_number, quantity));
    }

    public void delete(String bnNumber, int orderId) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s and order_id = %d", tableName, bnNumber, orderId));
    }

    public Order find(String bnNumber, int orderId) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s WHERE bn_number = %s and order_id = %d", columnsString,
                bnNumber, orderId));
        Order order = new Order();
        while (resultSet.next()){
            order.addProduct(resultSet.getInt("catalog_number"), resultSet.getInt("quantity"));
        }
        return order;
    }

    public List<Order> findAll(String bnNumber, int maxOrderId) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s WHERE bn_number = %s and order_id = %d", columnsString,
                bnNumber));
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < maxOrderId; i++)
            orderList.add(new Order());
        while (resultSet.next()){
            //orderList.get(resultSet.getInt("order_id")).addProduct(resultSet.getString("catalog_number"), resultSet.getInt("quantity"));
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> orderList = new ArrayList<>(3);
        orderList.add(2, "hello");
    System.out.println(orderList);
    }
}
