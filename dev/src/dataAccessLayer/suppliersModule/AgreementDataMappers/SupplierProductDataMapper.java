package dataAccessLayer.suppliersModule.AgreementDataMappers;

import businessLayer.suppliersModule.Product;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class SupplierProductDataMapper extends AbstractDataMapper {
    public SupplierProductDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "suppliers_products", new String[]{"bn_number", "catalog_number", "suppliers_catalog_number", "quantity", "price"});
    }

    public void insert(String bnNumber, String catalogNumber, String suppliersCatalogNumber, int quantity, double price) throws DalException {
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', '%s', %d, %f)",tableName, columnsString, bnNumber,
                    catalogNumber, suppliersCatalogNumber, quantity, price));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void delete(String bnNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void delete(String bnNumber, String catalogNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s' and catalog_number = '%s'", tableName, bnNumber, catalogNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public List<Product> find(String bnNumber) throws DalException {
        try {
            String columnsString = String.join(", ", columns);
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
            List<Product> products = new LinkedList<>();
            while (resultSet.next()){
                products.add(new Product(resultSet.getString("catalog_number"), resultSet.getString("suppliers_catalog_number"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity")));
            }
            return products;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateBnNumber(String bnNumber,String newBnNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET bn_number = '%s' WHERE bn_number = '%s'", tableName, newBnNumber, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateSuppliersCatalogNumber(String bnNumber, String catalogNumber, String suppliersCatalogNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET suppliers_catalog_number = '%s' WHERE bn_number = '%s' and catalog_number = '%s'", tableName, suppliersCatalogNumber, bnNumber, catalogNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateQuantity(String bnNumber, String catalogNumber, int quantity) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET quantity = %d WHERE bn_number = '%s' and catalog_number = '%s'", tableName, catalogNumber, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updatePrice(String bnNumber, String catalogNumber, double price) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET price = %f WHERE bn_number = '%s' and catalog_number = '%s'", tableName, price, catalogNumber, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
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
                .addColumn("bn_number", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addColumn("catalog_number", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addColumn("suppliers_catalog_number", ColumnType.TEXT)
                .addColumn("quantity", ColumnType.INTEGER)
                .addColumn("price", ColumnType.REAL)
                .addForeignKey("catalog_number", "products","catalog_number", ON_UPDATE.CASCADE, ON_DELETE.CASCADE)
                .addForeignKey("bn_number", "suppliers","bn_number", ON_UPDATE.CASCADE, ON_DELETE.CASCADE);
    }
}
