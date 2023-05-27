package dataAccessLayer.suppliersModule.OrderHistoryDataMappers;

import businessLayer.suppliersModule.Order;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.*;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class OrderHistoryDataMapper extends AbstractDataMapper {
    private final NumberOfOrdersCounterDataMapper numberOfOrdersCounterDataMapper;
    private final Map<String, List<Order>> suppliersOrderHistory;

    public OrderHistoryDataMapper(SQLExecutor sqlExecutor, NumberOfOrdersCounterDataMapper numberOfOrdersCounterDataMapper) throws DalException {
        super(sqlExecutor, "orders_history", new String[]{"bn_number", "order_id", "catalog_number", "quantity"});
        this.numberOfOrdersCounterDataMapper = numberOfOrdersCounterDataMapper;
        suppliersOrderHistory = new HashMap<>();
    }

    private void insert(String bnNumber, String catalog_number, int quantity, int orderId) throws DalException {
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d, '%s', %d)",tableName, columnsString, bnNumber,
                    orderId, catalog_number, quantity));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }
    public void insert(String bnNumber, Order order) throws DalException {
        int orderId = numberOfOrdersCounterDataMapper.getAndIncrement(bnNumber);
        if(orderId == 0) {
            suppliersOrderHistory.put(bnNumber, new LinkedList<>());
        } else if(!suppliersOrderHistory.containsKey(bnNumber)) {
            suppliersOrderHistory.put(bnNumber, findAll(bnNumber, orderId));
        }

        Map<String, Integer> products = order.getProducts();
        for(Map.Entry<String, Integer> productOrder : products.entrySet()) {
            insert(bnNumber, productOrder.getKey(), productOrder.getValue(), orderId);
        }
        suppliersOrderHistory.get(bnNumber).add(order);
    }

    public void delete(String bnNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public Order find(String bnNumber, int orderId) throws DalException {
        try {
            String columnsString = String.join(", ", columns);
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s' and order_id = %d", columnsString,
                    tableName, bnNumber, orderId));
            Order order = new Order();
            while (resultSet.next()){
                order.addProduct(resultSet.getString("catalog_number"), resultSet.getInt("quantity"));
            }
            return order;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public List<Order> findAll(String bnNumber, int numberOfOrders) throws DalException {
        try {
            String columnsString = String.join(", ", columns);
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString,
                    tableName, bnNumber));
            List<Order> orderList = new ArrayList<>();
            for (int i = 0; i < numberOfOrders; i++) {
                orderList.add(new Order());
            }
            while (resultSet.next()){
                orderList.get(resultSet.getInt("order_id")).addProduct(resultSet.getString("catalog_number"), resultSet.getInt("quantity"));
            }
            return orderList;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public List<Order> getOrderHistory(String bnNumber) throws DalException {
        if(suppliersOrderHistory.containsKey(bnNumber)) {
            return new LinkedList<>(suppliersOrderHistory.get(bnNumber));
        }
        int numberOfOrders = numberOfOrdersCounterDataMapper.getOrderNumber(bnNumber);
        List<Order> orderHistory = findAll(bnNumber, numberOfOrders);
        suppliersOrderHistory.put(bnNumber, orderHistory);
        return new LinkedList<>(orderHistory);
    }

    /**
     * Used to insert data into {@link AbstractDataMapper#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder
                .addColumn("bn_number", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("order_id", ColumnType.INTEGER, ColumnModifier.PRIMARY_KEY)
                .addColumn("catalog_number", ColumnType.TEXT)
                .addColumn("quantity", ColumnType.INTEGER)
                .addForeignKey("bn_number", "suppliers", "bn_number",
                        ON_UPDATE.CASCADE, ON_DELETE.CASCADE)
                .addForeignKey("catalog_number", "products", "catalog_number",
                        ON_UPDATE.CASCADE, ON_DELETE.NO_ACTION);
    }

    @Override
    public void clearTable(){
        super.clearTable();
        suppliersOrderHistory.clear();
    }
}
