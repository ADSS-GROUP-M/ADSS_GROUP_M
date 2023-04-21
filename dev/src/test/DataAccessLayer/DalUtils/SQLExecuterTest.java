package DataAccessLayer.DalUtils;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class SQLExecuterTest {

    @Test
    void executeRead() {
        String query = "SELECT * FROM EMPLOYEES";
        SQLExecuter executer = new SQLExecuter();
        try {
            LinkedList<Object[]> rows = executer.executeRead(query);
            for(Object[] row : rows){
                for(Object cell : row){
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    void executeWrite() {
    }
}