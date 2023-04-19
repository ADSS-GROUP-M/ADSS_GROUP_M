package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO {

    private final static String TABLE_NAME = "USERS";

    public void create(UserDTO userDto) {
        try {
            String queryString = "INSERT INTO "+TABLE_NAME+"(?, ?, ?, ?) VALUES(?,?,?,?)";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, userDto.getUsername());
            ptmt.setString(2, userDto.getPassword());
            ptmt.setBoolean(3, userDto.getIsLoggedIn());
            ptmt.executeUpdate();
            System.out.println("Data Added Successfully (UserDAO)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}