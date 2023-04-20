package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO extends DAO {

    public final String usernameColumnName = "Username";
    public final String passwordColumnName = "Password";
    public final String isLoggedInColumnName = "IsOnline";


    public UserDAO() throws SQLException{
        super("USERS");
    }

    public List<UserDTO> SelectAllUsers() throws SQLException {
        List<UserDTO> result = new LinkedList<>();
        for(DTO t :selectAll())
        {
            result.add((UserDTO) result);
        }

        return result;
    }
    public UserDTO select(int id) {
       return ((UserDTO)select(id));
    }

    protected DTO convertReaderToObject(ResultSet reader){
        UserDTO res = null;
        try {
            res = new UserDTO(this, reader.getInt(this.idColumnName),reader.getString(this.usernameColumnName),reader.getString(this.passwordColumnName),
                    reader.getInt(this.isLoggedInColumnName));
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public void create(UserDTO userDto) throws SQLException {
        try {
            String queryString = String.format("INSERT INTO "+TABLE_NAME+"( %s ,%s, %s, %s) VALUES(?,?,?,?)",
                    idColumnName, usernameColumnName, passwordColumnName, isLoggedInColumnName);
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, userDto.getId());
            ptmt.setString(2, userDto.getUsername());
            ptmt.setString(3, userDto.getPassword());
            ptmt.setBoolean(4, userDto.isLoggedIn());
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