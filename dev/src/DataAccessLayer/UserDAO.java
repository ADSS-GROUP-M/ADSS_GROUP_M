package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO<User> {

    private final static String TABLE_NAME = "USERS";

    public UserDAO(JdbcTemplate jdbcTemplate) {
        super(new UserRowMapper(), TABLE_NAME, jdbcTemplate);
    }

    private static class UserRowMapper implements RowMapper<User> {
        public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserDTO user = new UserDTO();
            userDto.setUserName(rs.getString("username"));
            userDto.setFirstName(rs.getString("fname"));
            user.setLastName(rs.getString("lname"));

            return user;
        }
    }
}