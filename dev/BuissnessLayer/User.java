package dev.BuissnessLayer;


public class User {

    private String username;
    private String password;
    private Employee linkedEmployee;
    private boolean isHrManager;
    private EmployeeController empController;

    public User(String name, String pass, Employee emp, boolean hr){
        this.username = name;
        this.password = pass;
        this.linkedEmployee = emp;
        this.isHrManager = hr;
        empController  = EmployeeController.getInstance();
    }

    public EmployeeController getEmployeeController(){

    }

    public void linkToEmployee(int id){
        empController.getEmployee(id);
    }

    public String getUsername() {
		return this.username;
	}

	public boolean getIsHrManager() {
		return this.isHrManager;
	}
}
	
