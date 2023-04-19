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
        this.setId(id);
        this.username = username;
        this.password = password;
        this.loggedIn = isLoggedIn != 0;
        this.controller.create(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws SQLException{
        this.controller.update(this.getId(), this.controller.usernameColumnName, username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws SQLException {
        this.controller.update(this.getId(),this.controller.passwordColumnName, password);
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.controller.update(this.getId(),this.controller.isLoggedInColumnName, loggedIn);
        this.loggedIn = loggedIn;
    }

}
