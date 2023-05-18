package dataAccessLayer.dalUtils;

public abstract class AbstractDataMapper{
    protected String tableName;
    protected String[] columns;
    protected SQLExecutorProductionImpl sqlExecutor;

    public AbstractDataMapper(String tableName, String[] columns) {
        this.tableName = tableName;
        this.columns = columns;
        sqlExecutor = new SQLExecutorProductionImpl("super_lee.sqlite");
    }

    public String getTableName(){
        return tableName;
    }
}
