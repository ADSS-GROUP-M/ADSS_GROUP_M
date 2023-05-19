package dataAccessLayer.dalUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CreateTableQueryBuilder {
    private final String tableName;
    private final ArrayList<Column> table_columns;
    private final ArrayList<String> primaryKeys;
    private final ArrayList<ForeignKey> foreignKeys;
    private final ArrayList<String> checks;
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

    public enum ON_DELETE{
        CASCADE,
        SET_NULL,
        SET_DEFAULT,
        RESTRICT,
        NO_ACTION;

        @Override
        public String toString() {
            return "ON DELETE "+super.toString().replace("_"," ");
        }
    }

    public enum ON_UPDATE{
        CASCADE,
        SET_NULL,
        SET_DEFAULT,
        RESTRICT,
        NO_ACTION;

        @Override
        public String toString() {
            return "ON UPDATE "+super.toString().replace("_"," ");
        }
    }

    public CreateTableQueryBuilder(String tableName){
        this.tableName = tableName;
        table_columns = new ArrayList<>();
        primaryKeys = new ArrayList<>();
        foreignKeys = new ArrayList<>();
        checks = new ArrayList<>();
        query = new StringBuilder();
    }

    public CreateTableQueryBuilder addColumn(String columnName, ColumnType type, ColumnModifier ... modifiers) {
        return addColumn(columnName, type, null, modifiers);
    }

    public CreateTableQueryBuilder addColumn(String columnName, ColumnType type, String _default, ColumnModifier ... modifiers){

        boolean isPrimaryKey = Arrays.asList(modifiers).contains(ColumnModifier.PRIMARY_KEY);
        if(isPrimaryKey){
            primaryKeys.add(columnName);
        }

        modifiers = Arrays.stream(modifiers)
                .distinct()
                .filter(modifier -> modifier != ColumnModifier.PRIMARY_KEY)
                .toArray(ColumnModifier[]::new);

        table_columns.add(new Column(columnName, type, _default, modifiers));
        return this;
    }

    /**
     * This method adds a composite foreign key
     *
     * @param columnNames      the columns' names in the current table
     * @param parentTableName the name of the parent table
     * @param referenceColumns the columns' names in the parent table
     */
    public CreateTableQueryBuilder addCompositeForeignKey(String[] columnNames, String parentTableName, String[] referenceColumns) {
        foreignKeys.add(new ForeignKey(parentTableName, columnNames, referenceColumns, null, null));
        return this;
    }


    /**
     * This method adds a composite foreign key
     *
     * @param columnNames      the columns' names in the current table
     * @param parentTableName  the name of the parent table
     * @param referenceColumns the columns' names in the parent table
     * @param onUpdate         the action to be taken when the parent column is updated
     */
    public CreateTableQueryBuilder addCompositeForeignKey(String[] columnNames, String parentTableName, String[] referenceColumns,  ON_UPDATE onUpdate) {
        return addCompositeForeignKey(columnNames, parentTableName, referenceColumns, null, onUpdate);
    }

    /**
     * This method adds a composite foreign key
     *
     * @param columnNames      the columns' names in the current table
     * @param parentTableName  the name of the parent table
     * @param referenceColumns the columns' names in the parent table
     * @param onDelete         the action to be taken when the parent column is deleted
     */
    public CreateTableQueryBuilder addCompositeForeignKey(String[] columnNames, String parentTableName, String[] referenceColumns, ON_DELETE onDelete) {
        return addCompositeForeignKey(columnNames, parentTableName, referenceColumns, onDelete, null);
    }

    /**
     * This method adds a composite foreign key
     *
     * @param columnNames      the columns' names in the current table
     * @param parentTableName the name of the parent table
     * @param referenceColumns the columns' names in the parent table
     * @param onDelete       the action to be taken when the parent column is deleted
     * @param onUpdate       the action to be taken when the parent column is updated
     */
    public CreateTableQueryBuilder addCompositeForeignKey(String[] columnNames, String parentTableName, String[] referenceColumns, ON_UPDATE onUpdate, ON_DELETE onDelete){
        return addCompositeForeignKey(columnNames, parentTableName, referenceColumns, onDelete, onUpdate);
    }

    /**
     * This method adds a composite foreign key
     *
     * @param columnNames      the columns' names in the current table
     * @param parentTableName the name of the parent table
     * @param referenceColumns the columns' names in the parent table
     * @param onDelete       the action to be taken when the parent column is deleted
     * @param onUpdate       the action to be taken when the parent column is updated                      
     */
    public CreateTableQueryBuilder addCompositeForeignKey(String[] columnNames, String parentTableName, String[] referenceColumns, ON_DELETE onDelete, ON_UPDATE onUpdate){
        foreignKeys.add(new ForeignKey(parentTableName, columnNames, referenceColumns, onDelete, onUpdate));
        return this;
    }

    /**
     * This method adds a non-composite foreign key
     *
     * @param columnName      the column name in the current table
     * @param parentTableName the name of the parent table
     * @param referenceColumn the column name in the parent table
     * @param onUpdate       the action to be taken when the parent column is updated
     */
    public CreateTableQueryBuilder addForeignKey(String columnName, String parentTableName, String referenceColumn,  ON_UPDATE onUpdate) {
        return addForeignKey(columnName, parentTableName, referenceColumn, null, onUpdate);
    }

    /**
     * This method adds a non-composite foreign key
     *
     * @param columnName      the column name in the current table
     * @param parentTableName the name of the parent table
     * @param referenceColumn the column name in the parent table
     * @param onDelete      the action to be taken when the parent column is deleted
     */
    public CreateTableQueryBuilder addForeignKey(String columnName, String parentTableName, String referenceColumn, ON_DELETE onDelete) {
        return addForeignKey(columnName, parentTableName, referenceColumn, onDelete, null);
    }

    /**
     * This method adds a non-composite foreign key
     *
     * @param columnName      the column name in the current table
     * @param parentTableName the name of the parent table
     * @param referenceColumn the column name in the parent table
     */
    public CreateTableQueryBuilder addForeignKey(String columnName, String parentTableName, String referenceColumn) {
        foreignKeys.add(new ForeignKey(parentTableName, new String[]{columnName}, new String[]{referenceColumn}, null,null));
        return this;
    }

    /**
     * This method adds a non-composite foreign key
     *
     * @param columnName      the column name in the current table
     * @param parentTableName the name of the parent table
     * @param referenceColumn the column name in the parent table
     * @param onUpdate      the action to be taken when the parent column is deleted
     * @param onDelete       the action to be taken when the parent column is updated
     */
    public CreateTableQueryBuilder addForeignKey(String columnName, String parentTableName, String referenceColumn, ON_UPDATE onUpdate, ON_DELETE onDelete){
        return addForeignKey(columnName, parentTableName, referenceColumn, onDelete, onUpdate);
    }

    /**
     * This method adds a non-composite foreign key
     *
     * @param columnName      the column name in the current table
     * @param parentTableName the name of the parent table
     * @param referenceColumn the column name in the parent table
     * @param onUpdate      the action to be taken when the parent column is deleted
     * @param onDelete       the action to be taken when the parent column is updated
     */
    public CreateTableQueryBuilder addForeignKey(String columnName, String parentTableName, String referenceColumn, ON_DELETE onDelete, ON_UPDATE onUpdate){
        foreignKeys.add(new ForeignKey(parentTableName, new String[]{columnName}, new String[]{referenceColumn}, onDelete, onUpdate));
        return this;
    }

    /**
     * @param check predicate
     */
    public CreateTableQueryBuilder addCheck(String check){
        checks.add(check);
        return this;
    }

    public String buildQuery(){
        query.append(String.format("CREATE TABLE IF NOT EXISTS %s (\n", tableName));
        buildAllColumns();
        buildPrimaryKeys();
        buildForeignKeys();
        buildChecks();
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
            if(column._default != null && column._default.isEmpty() == false){
                query.append(String.format(" DEFAULT %s", column._default));
            }
            query.append(",\n");
        }
    }

    private void buildForeignKeys() {
        for(ForeignKey fk : foreignKeys){
            query.append(String.format("CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES \"%s\"(%s)",
                    generateForeignKeyName(fk),
                    columnsArrayToString(fk.columns),
                    fk.parentTableName,
                    columnsArrayToString(fk.references)));
            if(fk.onDelete != null){
                query.append(" ").append(fk.onDelete);
            }
            if(fk.onUpdate != null){
                query.append(" ").append(fk.onUpdate);
            }
            query.append(",\n");
        }
    }

    private String generateForeignKeyName(ForeignKey fk) {
        StringBuilder name = new StringBuilder();
        name.append(fk.parentTableName);
        for (String columnName : fk.columns) {
            name.append("_");
            name.append(table_columns.indexOf(new Column(columnName))+1);
        }
        return name.toString();
    }

    private void buildPrimaryKeys() {
        if(primaryKeys.isEmpty() == false){
            query.append(String.format("PRIMARY KEY(%s),\n",
                    columnsArrayToString(primaryKeys.toArray(new String[0]))));
        }
    }

    private void buildChecks() {
        int i = 1;
        for(String check : checks){
            query.append(String.format("CONSTRAINT CHK_%d CHECK (%s),\n",i++, check));
        }
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

    private record Column (String name, ColumnType type, String _default, ColumnModifier[] modifiers){

        public Column(String name) {
            this(name, null,null, null);
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

    private record ForeignKey(String parentTableName, String[] columns, String[] references, ON_DELETE onDelete, ON_UPDATE onUpdate){

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
