package dataAccessLayer.dalUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public class OfflineResultSet {

    private final LinkedList<HashMap<String,Object>> rows;
    private final String[] columnNames;
    private final int columnCount;
    private int currentRow;
    private HashMap<String,Object> currentRowData;
    private ListIterator<HashMap<String,Object>> iterator;

    public OfflineResultSet(ResultSet rs) throws SQLException {
        currentRow = -1;
        ResultSetMetaData metaData = rs.getMetaData();
        columnCount = metaData.getColumnCount();
        columnNames = new String[columnCount];
        for(int i = 0; i < columnCount ; i++){
            columnNames[i] = metaData.getColumnName(i+1);
        }

        rows = new LinkedList<>();
        while (rs.next()){
            HashMap<String,Object> row = new HashMap<>();
            for(int i = 0; i < columnCount ; i++){
                row.put(columnNames[i],rs.getObject(i+1));
            }
            rows.add(row);
        }
    }

    public boolean next() {
        if(iterator == null){
            iterator = rows.listIterator();
        }
        if(iterator.hasNext()){
            currentRow++;
            currentRowData = iterator.next();
            return true;
        }
        return false;
    }

    public boolean previous() {
        if(iterator == null){
            iterator = rows.listIterator();
        }
        if(iterator.hasPrevious()){
            currentRow--;
            currentRowData = iterator.previous();
            return true;
        }
        return false;
    }

    public void reset() {
        iterator = null;
        currentRow = -1;
        currentRowData = null;
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }

    public Object getObject(String columnName) {
        return currentRowData.get(columnName);
    }

    public String getString(String columnName) {
        return (String) currentRowData.get(columnName);
    }

    public int getInt(String columnName) {
        return (int) currentRowData.get(columnName);
    }

    public boolean getBoolean(String columnName) {
        return (Boolean) currentRowData.get(columnName);
    }

    public LocalDateTime getLocalDateTime(String columnName) {
        return LocalDateTime.parse(getString(columnName), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public LocalDate getLocalDate(String columnName) {
        return LocalDate.parse(getString(columnName), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public LocalTime getLocalTime(String columnName) {
        return LocalTime.parse(getString(columnName), DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getCurrentRow() {
        return currentRow;
    }

}
