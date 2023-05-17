package dataAccessLayer.dalUtils;

import java.util.*;

public class CreateTableQueryBuilder {
    private final String tableName;
    private final ArrayList<Column> table_columns;
    private final ArrayList<String> primaryKeys;
    private final ArrayList<ForeignKey> foreignKeys;
    private final StringBuilder query;

    public enum ColumnType{
        INTEGER,
        TEXT,
        REAL,
        BLOB,
        NUMERIC
    }

    public enum ColumnModifier{
        NOT_NULL,
        UNIQUE,
        PRIMARY_KEY,
        AUTO_INCREMENT;

        @Override
        public String toString() {
            return super.toString().replace("_"," ");
        }
    }

    public CreateTableQueryBuilder(String tableName){
        this.tableName = tableName;
        table_columns = new ArrayList<>();
        primaryKeys = new ArrayList<>();
        foreignKeys = new ArrayList<>();
        query = new StringBuilder();
    }

    public void addColumn(String columnName, ColumnType type, ColumnModifier ... modifiers ){

        boolean isPrimaryKey = Arrays.asList(modifiers).contains(ColumnModifier.PRIMARY_KEY);
        if(isPrimaryKey){
            primaryKeys.add(columnName);
        }

        modifiers = Arrays.stream(modifiers)
                .distinct()
                .filter(modifier -> modifier != ColumnModifier.PRIMARY_KEY)
                .toArray(ColumnModifier[]::new);

        table_columns.add(new Column(columnName, type, modifiers));
    }

    /**
     * @param columnNames the column name in the current table
     * @param referenceColumns the column name in the parent table
     */
    public void addForeignKey(String parentTableName, String[] columnNames, String[] referenceColumns){
        foreignKeys.add(new ForeignKey(parentTableName, columnNames, referenceColumns));
    }

    public String buildQuery(){
        query.append(String.format("CREATE TABLE IF NOT EXISTS %s (\n", tableName));
        buildAllColumns();
        buildPrimaryKeys();
        buildForeignKeys();
        query.delete(query.length() - 2, query.length() - 1); // remove the last comma
        query.append(");");
        return query.toString();
    }

    private void buildAllColumns() {
        for (Column column : table_columns) {
            query.append(String.format("\"%s\" %s", column.name, column.type));
            for (ColumnModifier modifier : column.modifiers) {
                query.append(String.format(" %s", modifier));
            }
            query.append(",\n");
        }
    }

    private void buildForeignKeys() {
        for(ForeignKey fk : foreignKeys){
            query.append(String.format("CONSTRAINT FK_%s FOREIGN KEY(%s) REFERENCES \"%s\"(%s),\n",
                    generateForeignKeyName(fk),
                    columnsArrayToString(fk.columns),
                    fk.parentTableName,
                    columnsArrayToString(fk.references)));
        }
    }

    private String generateForeignKeyName(ForeignKey fk) {
        StringBuilder name = new StringBuilder();
        name.append(fk.parentTableName);
        for (String columnName : fk.columns) {
            name.append("_");
            name.append(table_columns.indexOf(new Column(columnName)));
        }
        return name.toString();
    }

    private void buildPrimaryKeys() {
        query.append(String.format("PRIMARY KEY(%s),\n",
                columnsArrayToString(primaryKeys.toArray(new String[0]))));
    }

    private String columnsArrayToString(String[] columns) {
        StringBuilder columnsString = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            columnsString.append(String.format("\"%s\"", columns[i]));
            if (i != columns.length - 1) {
                columnsString.append(",");
            }
        }
        return columnsString.toString();
    }

    private record Column (String name, ColumnType type, ColumnModifier[] modifiers){

        public Column(String name) {
            this(name, ColumnType.TEXT, new ColumnModifier[]{});
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Column column = (Column) o;
            return Objects.equals(name, column.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    private record ForeignKey(String parentTableName, String[] columns, String[] references){

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ForeignKey that = (ForeignKey) o;
            return Objects.equals(parentTableName, that.parentTableName)
                    && Arrays.equals(columns, that.columns)
                    && Arrays.equals(references, that.references);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(parentTableName);
            result = 31 * result + Arrays.hashCode(columns);
            result = 31 * result + Arrays.hashCode(references);
            return result;
        }
    }

}
