package dev.BusinessLayer.Employees;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import dev.BusinessLayer.Employees.Shift.ShiftTime;

class Employee{
   private String name;
   private String id;
   private BankDetails bankAccount;
   private int salary;
   private List<String> empConditions;
   private LocalDate employmentDate;
   private Constraints workingConstraints;
   private boolean isFired;

   private class Constraints {
      //private List<Integer> unavailableWeekDays;
      private List<Role> availableRoles;
      private List<Branch> availableBranches;
      private List<LocalDate> unavailableDates;
      private static final int MAX_WORKDAYS_A_WEEK = 6;
      private static final int MAX_SHIFTS_A_DAY = 1;
      private Employee employee;
  
      public Constraints(Employee emp){
          //unavailableWeekDays = new LinkedList<Integer>();
          availableRoles = new ArrayList<>();
          availableRoles.add(Role.GENERAL_WORKER);
          availableBranches = Branch.getAllBranches();
          this.employee = emp;
          this.unavailableDates = new ArrayList<>();
      }
  
      public boolean checkScheduleLegality(LocalDate from, LocalDate until){
          List<Branch> branches = Branch.getAllBranches();
          int workDaysAWeek = 0;
          int shiftsADay = 0;
  
          LocalDate current = from;
          while(current.equals(from) || (current.isAfter(from) && current.isBefore(until))){
              boolean worksShiftTime = false;
              boolean isWorkingThisDay = false;
              for(Branch b: branches){
                  Shift sh1 = Shift.getInstance(ShiftTime.MORNING, current, b);
                  if(sh1.isEmployeeWorking(employee)){
                      if(!b.isWorkingDay(current))
                          return false; // registered to a shift when the branch is not working on this day
                      if(worksShiftTime)
                          return false;//employee scheduled to 2 shifts that happen simultaniously
                      else{
                          worksShiftTime = true;
                          isWorkingThisDay = true;
                          shiftsADay ++;
                          for(Role r: sh1.getRoles(employee)){
                              if(!availableRoles.contains(r))
                                  return false; //unavailable role
                          }
                          if(!availableBranches.contains(sh1.getBranch()))
                              return false;// unavailable branch
                      }
                  }
              }
              worksShiftTime = false;
              for(Branch b:branches){
                  Shift sh2 = Shift.getInstance(ShiftTime.EVENING, current, b);
                  if(sh2.isEmployeeWorking(employee)){
                      if(!b.isWorkingDay(current))
                          return false; // registered to a shift when the branch is not working on this day
                      if(worksShiftTime)
                          return false;//employee scheduled to 2 shifts that happen simultaniously
                      else{
                              worksShiftTime = true;
                              isWorkingThisDay = true;
                              shiftsADay++;
                              for(Role r: sh2.getRoles(employee)){
                                  if(!availableRoles.contains(r))
                                      return false; //unavailable role
                              }
                              if(!availableBranches.contains(sh2.getBranch()))
                                  return false;// unavailable branch
                      }
                  }
              }
              if(isWorkingThisDay){
                  workDaysAWeek ++;
                  if(this.unavailableDates.contains(current))
                      return false; // working on a constrained date, either by self or by manager
              }
              if(shiftsADay>MAX_SHIFTS_A_DAY)
                  return false;// more shifts a day than possible
              shiftsADay = 0;
              if(current.getDayOfWeek() == DayOfWeek.SATURDAY){
                  if(workDaysAWeek>MAX_WORKDAYS_A_WEEK)
                      return false; //too many work days a week
                  workDaysAWeek = 0;
              }
              current = current.plusDays(1);
          }
          if(workDaysAWeek>MAX_WORKDAYS_A_WEEK)
              return false; //too many work days a week
          return true;
      }
  
      public void addUnavailableDate(LocalDate d){
          this.unavailableDates.add(d);
      }
  
      public void setAvailableDate(LocalDate d) {
          this.unavailableDates.remove(d);
      }
  }

  

   public Employee(String name, String id, BankDetails bd, List<String> ec, LocalDate employmentDate){
      this.name = name;
      this.id = id;
      this.bankAccount = bd;
      this.calculateSalary();
      this.empConditions = ec;
      this.employmentDate = employmentDate;
      this.workingConstraints = new Constraints(this);
      this.isFired = false;
   }

   public String getId() {
       return this.id;
   }

   public boolean getIsFired(){
      return isFired;
   }

   public void addUnavailableDate(LocalDate d){
      this.workingConstraints.addUnavailableDate(d);
  }

   public void setAvailableDate(LocalDate d){
      this.workingConstraints.setAvailableDate(d);
   }
   public int getSalary(){
      return this.salary;
   }
   public void setSalary(int x){
      this.salary = x;
   }
   public void calculateSalary(){
      this.salary = 5000;
   }
   public String toString(){
      
      String legalityWarning = "";
        if(!this.workingConstraints.checkScheduleLegality(LocalDate.now(), LocalDate.now().plusYears(1)))
            legalityWarning = "WARNING! shift does'nt meet constraints!";
      return "UNFINISHED";
   }
   public boolean checkLegality(LocalDate a, LocalDate b){
      return this.workingConstraints.checkScheduleLegality(a,b);
   }

   public void calculateSalaryAutomatically() throws Exception{
      throw new Exception("unimplented");
   }

   public void setIsFired(boolean is){
      this.isFired = is;
   }
}