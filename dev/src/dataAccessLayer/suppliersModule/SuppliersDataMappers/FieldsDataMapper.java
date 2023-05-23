package dataAccessLayer.suppliersModule.SuppliersDataMappers;


import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class FieldsDataMapper extends AbstractDataMapper {
        public FieldsDataMapper(SQLExecutor sqlExecutor) throws DalException {
                super(sqlExecutor, "fields", new String[] {"bn_number", "field"});
        }

        public void insert(String bnNumber, String field) throws SQLException {
                String columnsString = String.join(", ", columns);
                sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s')",tableName, columnsString, bnNumber,
                        field));
        }

        public void delete(String bnNumber) throws SQLException {
                sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
        }

        public void delete(String bnNumber, String field) throws SQLException {
                sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s' and field = '%s'", tableName, bnNumber, field));
        }

        public List<String> find(String bnNumber) throws SQLException {
                String columnsString = String.join(", ", columns);
                OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
                List<String> fields = new LinkedList<>();
                while (resultSet.next())
                        fields.add(resultSet.getString("field"));
                return fields;
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
                        .addColumn("field", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                        .addForeignKey("bn_number", "suppliers","bn_number", ON_UPDATE.CASCADE, ON_DELETE.CASCADE);
        }
}
