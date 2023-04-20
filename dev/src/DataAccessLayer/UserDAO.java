package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO extends DAO {

    public enum Columns {
        Username,
        Password,
        IsLoggedIn;
    }

    public UserDAO() throws SQLException{
        super("USERS", new String[] {Columns.Username.name(), Columns.Password.name()});
    }

    public List<UserDTO> SelectAllUsers() throws SQLException {
        List<UserDTO> result = new LinkedList<>();
        for(DTO t :selectAll())
        {
            result.add((UserDTO) result);
        }

        return result;
    }

    public UserDTO select(String username, String password) throws Exception {
        Object[] keys = {username,password};
        return ((UserDTO)select(keys));
    }

    public void update(String username, String password, String attributeName, String attributeValue) throws Exception
    {
        Object[] keys = {username,password};
        super.update(keys,attributeName,attributeValue);
    }

    public void update(String username, String password, String attributeName, Integer attributeValue) throws Exception
    {
        Object[] keys = {username,password};
        super.update(keys,attributeName,attributeValue);
    }

    public void update(String username, String password, String attributeName, boolean attributeValue) throws Exception
    {
        Object[] keys = {username,password};
        super.update(keys,attributeName,attributeValue);
    }

    public void delete(String username, String password)
    {
        Object[] keys = {username,password};
      super.delete(keys);
    }

    protected DTO convertReaderToObject(ResultSet reader){
        UserDTO res = null;
        try {
            res = new UserDTO(this,reader.getString(Columns.Username.name()),reader.getString(Columns.Password.name()),
                    reader.getInt(Columns.IsLoggedIn.name()));
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public void create(UserDTO userDto) throws SQLException {
        try {
            String queryString = String.format("INSERT INTO "+TABLE_NAME+"(%s, %s, %s) VALUES(?,?,?)",
                    Columns.Username.name(), Columns.Password.name(), Columns.IsLoggedIn.name());
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, userDto.getUsername());
            ptmt.setString(2, userDto.getPassword());
            ptmt.setBoolean(3, userDto.isLoggedIn());
            ptmt.executeUpdate();
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