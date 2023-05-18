package dataAccessLayer.dalAbstracts;

import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @param <T> The child Type
 */
public abstract class ManyToManyDAO<T> extends DAO<T>{

    protected final String[] PARENT_TABLE_NAME;
    protected final String[][] FOREIGN_KEYS;
    protected final String[][] REFERENCES;

    protected ManyToManyDAO(SQLExecutor cursor,
                            String tableName,
                            String[] parentTableName,
                            String[] types,
                            String[] primaryKeys,
                            String[][] foreignKeys,
                            String[][] references,
                            String ... allColumns) throws DalException{
        super(cursor,tableName,types,primaryKeys,allColumns);
        PARENT_TABLE_NAME = parentTableName;
        FOREIGN_KEYS = foreignKeys;
        REFERENCES = references;
    }

    /**
     * Initialize the table if it doesn't exist
     */
    protected void initTable() throws DalException{

        // columns
        for (int i = 0; i < ALL_COLUMNS.length; i++) {
            ArrayList<CreateTableQueryBuilder.ColumnModifier> modifiers = new ArrayList<>();

            // basic modifiers
            modifiers.add(CreateTableQueryBuilder.ColumnModifier.NOT_NULL);

            // primary key
            if(Arrays.asList(PRIMARY_KEYS).contains(ALL_COLUMNS[i])) {
                modifiers.add(CreateTableQueryBuilder.ColumnModifier.PRIMARY_KEY);
            }

            createTableQueryBuilder.addColumn(ALL_COLUMNS[i],
                    CreateTableQueryBuilder.ColumnType.valueOf(TYPES[i]),
                    modifiers.toArray(new CreateTableQueryBuilder.ColumnModifier[0]));
        }

        // foreign keys
        for(int i = 0; i < FOREIGN_KEYS.length ; i++){
            createTableQueryBuilder.addForeignKey( PARENT_TABLE_NAME[i],FOREIGN_KEYS[i], REFERENCES[i]);
        }

        String query = createTableQueryBuilder.buildQuery();
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+TABLE_NAME, e);
        }
    }
}
