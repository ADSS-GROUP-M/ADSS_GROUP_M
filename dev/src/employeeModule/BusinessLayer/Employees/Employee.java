package employeeModule.BusinessLayer.Employees;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Employee implements EmployeeInterface {
    private String name;
    private String id;
    private String bankDetails;
    private double hourlySalaryRate;
    private double monthlyHours;
    private double salaryBonus;
    private LocalDate employmentDate;
    private String employmentConditions;
    private String details;
    private Set<Role> roles;

  // private class Constraints {
  //    //private List<Integer> unavailableWeekDays;
  //    private List<Role> availableRoles;
  //    private List<Branch> availableBranches;
  //    private List<LocalDate> unavailableDates;
  //    private static final int MAX_WORKDAYS_A_WEEK = 6;
  //    private static final int MAX_SHIFTS_A_DAY = 1;
  //    private Employee employee;
  
  //    public Constraints(Employee emp){
  //        //unavailableWeekDays = new LinkedList<Integer>();
  //        availableRoles = new ArrayList<>();
  //        availableRoles.add(Role.GENERAL_WORKER);
  //        availableBranches = Branch.getAllBranches();
  //        this.employee = emp;
  //        this.unavailableDates = new ArrayList<>();
  //    }
  
  //    public boolean checkScheduleLegality(LocalDate from, LocalDate until){
  //        List<Branch> branches = Branch.getAllBranches();
  //        int workDaysAWeek = 0;
  //        int shiftsADay = 0;
  
  //        LocalDate current = from;
  //        while(current.equals(from) || (current.isAfter(from) && current.isBefore(until))){
  //            boolean worksShiftTime = false;
  //            boolean isWorkingThisDay = false;
  //            for(Branch b: branches){
  //                Shift sh1 = Shift.getInstance(ShiftTime.MORNING, current, b);
  //                if(sh1.isEmployeeWorking(employee)){
  //                    if(!b.isWorkingDay(current))
  //                        return false; // registered to a shift when the branch is not working on this day
  //                    if(worksShiftTime)
  //                        return false;//employee scheduled to 2 shifts that happen simultaniously
  //                    else{
  //                        worksShiftTime = true;
  //                        isWorkingThisDay = true;
  //                        shiftsADay ++;
  //                        for(Role r: sh1.getRoles(employee)){
  //                            if(!availableRoles.contains(r))
  //                                return false; //unavailable role
  //                        }
  //                        if(!availableBranches.contains(sh1.getBranch()))
  //                            return false;// unavailable branch
  //                    }
  //                }
  //            }
  //            worksShiftTime = false;
  //            for(Branch b:branches){
  //                Shift sh2 = Shift.getInstance(ShiftTime.EVENING, current, b);
  //                if(sh2.isEmployeeWorking(employee)){
  //                    if(!b.isWorkingDay(current))
  //                        return false; // registered to a shift when the branch is not working on this day
  //                    if(worksShiftTime)
  //                        return false;//employee scheduled to 2 shifts that happen simultaniously
  //                    else{
  //                            worksShiftTime = true;
  //                            isWorkingThisDay = true;
  //                            shiftsADay++;
  //                            for(Role r: sh2.getRoles(employee)){
  //                                if(!availableRoles.contains(r))
  //                                    return false; //unavailable role
  //                            }
  //                            if(!availableBranches.contains(sh2.getBranch()))
  //                                return false;// unavailable branch
  //                    }
  //                }
  //            }
  //            if(isWorkingThisDay){
  //                workDaysAWeek ++;
  //                if(this.unavailableDates.contains(current))
  //                    return false; // working on a constrained date, either by self or by manager
  //            }
  //            if(shiftsADay>MAX_SHIFTS_A_DAY)
  //                return false;// more shifts a day than possible
  //            shiftsADay = 0;
  //            if(current.getDayOfWeek() == DayOfWeek.SATURDAY){
  //                if(workDaysAWeek>MAX_WORKDAYS_A_WEEK)
  //                    return false; //too many work days a week
  //                workDaysAWeek = 0;
  //            }
  //            current = current.plusDays(1);
  //        }
  //        if(workDaysAWeek>MAX_WORKDAYS_A_WEEK)
  //            return false; //too many work days a week
  //        return true;
  //    }
  //
  //    public void addUnavailableDate(LocalDate d){
  //        this.unavailableDates.add(d);
  //    }
  //
  //    public void setAvailableDate(LocalDate d) {
  //        this.unavailableDates.remove(d);
  //    }
  //}

    public Employee(String name, String id, double hourlySalaryRate, String bankDetails, LocalDate employmentDate, String employmentConditions, String details){
       this.name = name;
       this.id = id;
       this.bankDetails = bankDetails;
       this.hourlySalaryRate = hourlySalaryRate;
       this.monthlyHours = 0;
       this.salaryBonus = 0;
       //calculateSalary();
       this.employmentDate = employmentDate;
       this.employmentConditions = employmentConditions;
       this.details = details;
       this.roles = new HashSet<>();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public LocalDate getEmploymentDate() {
        return this.employmentDate;
    }

    @Override
    public String getEmploymentConditions() {
        return employmentConditions;
    }

    @Override
    public void setEmploymentConditions(String employmentConditions) {
        this.employmentConditions = employmentConditions;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public String getBankDetails() {
        return this.bankDetails;
    }

    @Override
    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    @Override
    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public void addRole(Role role) {
        roles.add(role);
    }

    @Override
    public void removeRole(Role role) throws Exception {
        if (!roles.contains(role))
            throw new Exception("The given role: `" + role + "` is not one of the employee roles.");
        roles.remove(role);
    }

    @Override
    public Set<Role> getRoles() {
        return this.roles;
    }

    //public void addUnavailableDate(LocalDate d) {
    //   this.workingConstraints.addUnavailableDate(d);
    //}
    //
    //public void setAvailableDate(LocalDate d){
    //   this.workingConstraints.setAvailableDate(d);
    //}

    @Override
    public void setHourlySalaryRate(double newRate){
       this.hourlySalaryRate = newRate;
    }

    @Override
    public double getHourlySalaryRate() {
        return this.hourlySalaryRate;
    }

    @Override
    public void setSalaryBonus(double salaryBonus) {
        this.salaryBonus = salaryBonus;
    }

    @Override
    public double getSalaryBonus() {
        return this.salaryBonus;
    }

    @Override
    public void addMonthlyHours(double hours) {
        this.monthlyHours += hours;
    }

    @Override
    public void resetMonthlyHours() {
        this.monthlyHours = 0;
    }

    @Override
    public double getMonthlyHours() {
        return this.monthlyHours;
    }

    @Override
    public double calculateSalary() {
        return hourlySalaryRate * monthlyHours + salaryBonus;
    }

    //public String toString() {
    //   String legalityWarning = "";
    //     if(!this.workingConstraints.checkScheduleLegality(LocalDate.now(), LocalDate.now().plusYears(1)))
    //         legalityWarning = "WARNING! shift does'nt meet constraints!";
    //   return "UNFINISHED";
    //}
    //
    //public boolean checkLegality(LocalDate a, LocalDate b){
    //   return this.workingConstraints.checkScheduleLegality(a,b);
    //}
}