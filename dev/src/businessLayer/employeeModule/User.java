package businessLayer.employeeModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {// This class represents a user in the system and manages its authorization

    private String username;
    private String password;
    private boolean loggedIn;
    private Set<Authorization> authorizations;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        this.authorizations = new HashSet<>();
    }

    public User(String username, String password, Authorization auth){
        this(username, password);
        this.authorizations.add(auth);
    }

    public boolean login(String password) throws Exception {
        if (loggedIn)
            throw new Exception("The user is already logged in to the system.");
        if (checkPassword(password))
            loggedIn = true;
        return loggedIn;
    }

    public void logout() {
        if (!loggedIn)
            throw new RuntimeException("The user is already logged out.");
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

    public String getUsername() {
		return this.username;
	}

	public boolean isAuthorized(Authorization auth){
		return this.authorizations.contains(auth);
	}

    public List<Authorization> getAuthorizations() {
        return new ArrayList<>(this.authorizations); // Returns a copy of the user's authorization, as a list
    }

    public void authorize(Authorization auth) {
        this.authorizations.add(auth);
    }

    public String getPassword() {
        return this.password;
    }

    public void setAuthorizations(Set<Authorization> auth) {
        this.authorizations = auth;
    }
}
	
