package dataAccessLayer.suppliersModule.AgreementDataMappers;

import businessLayer.suppliersModule.DeliveryAgreements.DeliveryAgreement;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryByInvitation;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryFixedDays;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class DeliveryAgreementDataMapper  extends AbstractDataMapper {
    public DeliveryAgreementDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "delivery_agreement", new String[] {"bn_number", "have_transport", "by_invitation", "days"});
    }

    public void insert(String bnNumber, boolean haveTransport, boolean byInvitation, String days) throws DalException {
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d, %d, '%s')",tableName, columnsString, bnNumber,
                    haveTransport? 1 : 0, byInvitation? 1 : 0, days));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateHaveTransport(String bnNumber, boolean haveTransport) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET have_transport = %d WHERE bn_number = '%s'", tableName, haveTransport? 1 : 0, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateByInvitation(String bnNumber, boolean byInvitation) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET by_invitation = %d WHERE bn_number = '%s'", tableName, byInvitation? 1 : 0, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateDays(String bnNumber, String days) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET days = %s WHERE bn_number = '%s'", tableName, days));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public DeliveryAgreement find(String bnNumber) throws DalException {
        try {
            String columnsString = String.join(", ", columns);
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName,
                    bnNumber));
            if(resultSet.next()) {
                boolean byInvitation = resultSet.getInt("by_invitation") == 1;
                if (byInvitation) {
                    return new DeliveryByInvitation(resultSet.getInt("have_transport") == 1, Integer.parseInt(resultSet.getString("days")));
                }
                String[] daysString = resultSet.getString("days").split(",");
                List<Integer> days = new ArrayList<>();
                for (int i = 0; i < daysString.length; i++) {
                    days.add(Integer.parseInt(daysString[i]));
                }
                return new DeliveryFixedDays(resultSet.getInt("have_transport") == 1, days);
            }
            return null;
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
                .addColumn("bn_number", ColumnType.TEXT)
                .addColumn("have_transport", ColumnType.INTEGER)
                .addColumn("by_invitation", ColumnType.INTEGER)
                .addColumn("days", ColumnType.TEXT)
                .addCheck("have_transport >= 0 AND have_transport <= 1")
                .addCheck("by_invitation >= 0 AND by_invitation <= 1")
                .addForeignKey("bn_number", "suppliers", "bn_number",
                        ON_UPDATE.CASCADE, ON_DELETE.CASCADE);
    }
}
