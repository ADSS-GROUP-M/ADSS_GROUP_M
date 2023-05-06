package dataAccessLayer.transportModule.abstracts;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.SQLExecutor;

import java.sql.SQLException;

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
        StringBuilder query = new StringBuilder();

        query.append(String.format("CREATE TABLE IF NOT EXISTS %s (\n", TABLE_NAME));

        for (int i = 0; i < ALL_COLUMNS.length; i++) {
            query.append(String.format("\"%s\" %s NOT NULL,\n", ALL_COLUMNS[i], TYPES[i]));
        }

        query.append("PRIMARY KEY(");
        for(int i = 0; i < PRIMARY_KEYS.length; i++) {
            query.append(String.format("\"%s\"",PRIMARY_KEYS[i]));
            if (i != PRIMARY_KEYS.length-1) {
                query.append(",");
            } else {
                query.append("),\n");
            }
        }

        for(int i = 0; i < FOREIGN_KEYS.length ; i++){
            query.append(String.format("CONSTRAINT FK_%s FOREIGN KEY(%s) REFERENCES \"%s\"(%s)",
                    PARENT_TABLE_NAME[i],
                    generateForeignKeys(i),
                    PARENT_TABLE_NAME[i],
                    generateReferences(i)
            ));
            if(i != FOREIGN_KEYS.length-1){
                query.append(",\n");
            } else {
                query.append("\n");
            }
        }

        query.append(");");

        try {
            cursor.executeWrite(query.toString());
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+TABLE_NAME, e);
        }
    }

    private String generateForeignKeys(int i) {
        StringBuilder foreignKeys = new StringBuilder();
        for (int j = 0; j < FOREIGN_KEYS[i].length; j++) {
            foreignKeys.append(String.format("\"%s\"", FOREIGN_KEYS[i][j]));
            if (j != FOREIGN_KEYS[i].length - 1) {
                foreignKeys.append(",");
            }
        }
        return foreignKeys.toString();
    }

    private String generateReferences(int i) {
        StringBuilder references = new StringBuilder();
        for (int j = 0; j < REFERENCES[i].length; j++) {
            references.append(String.format("\"%s\"", REFERENCES[i][j]));
            if (j != REFERENCES[i].length - 1) {
                references.append(",");
            }
        }
        return references.toString();
    }
}
