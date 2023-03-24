import java.util.*;

public class EmployeeController{

    private List<Employee> workingEmployees;
    private List<Employee> firedEmployees;
    private static EmployeeController instance;

    private EmployeeController(){
        workingEmployees = new LinkedList<Employee>();
        firedEmployees = new LinkedList<Employee>();
    }

    public static EmployeeController getInstance(User requestFrom)
    {
        if(instance == null)
            instance = new EmployeeController();
        return instance;
    }

    public void recruitEmployee(User requestFrom, String name, int id, BankDetails bd, int salary, List<EmploymentConditions> ec, Date employmentDate){
        if(requestFrom.getIsHrManager()){
            Employee emp = new Employee(name,id,bd,salary,ec, employmentDate);
            this.workingEmployees.add(emp);
            return;
        } 
        else
            throw new Exception("This user isn't authorized to do this.");
    }

    public void fireEmployee(User requestFrom, int id){
        if(requestFrom.getIsHrManager()){
            for(Employee e: workingEmployees){
                if(e.getId() == id){
                    firedEmployees.add(e);
                    workingEmployees.remove(e);
                    return;
                }
            }
            throw new Exception("Employee is not found or already fired");    
        }
        else
            throw new Exception("This user isn't authorized to do this.");
    }

    public void scheduleShift(User requestFrom, Date d, ShiftTime st, Branch br){// in a branch
        throw new UnimplementedException();
    }

    public boolean setRequiredRoles(User requestFrom){ // in a shift/in a branch/on a date
        throw new UnimplementedException();
    }

    public boolean setAvailabilityConstraint(User requestFrom){
        throw new UnimplementedException();
    }

    public Employee getEmployee(int id){
        for(Employee e: this.allEmployees){
            if(e.getId() == id)
                return e;
        }
        throw new Exception("Employee not found");
    }

    
}