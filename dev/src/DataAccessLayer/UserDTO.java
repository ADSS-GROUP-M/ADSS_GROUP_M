package DataAccessLayer;

import java.sql.SQLException;

public class UserDTO extends DTO<UserDAO> {
    private String username;
    private String password;
    private boolean loggedIn;

    public UserDTO() throws SQLException {
        super(new UserDAO());

    }

    public UserDTO(UserDAO dao, int id, String username, String pass, int isLoggedIn) throws SQLException{
        super(dao);
        this.username = username;
        this.password = password;
        this.loggedIn = isLoggedIn != 0;
        this.controller.create(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) throws Exception{
        this.controller.update(username, password, UserDAO.Columns.Username.name(), newUsername);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) throws Exception {
        this.controller.update(username, password, UserDAO.Columns.Password.name(), newPassword);
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.controller.update(username, password, UserDAO.Columns.Username.name(), newUsername);
        this.loggedIn = loggedIn;
    }

}
