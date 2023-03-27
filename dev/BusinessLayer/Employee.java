package dev.BusinessLayer;

import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import dev.BusinessLayer.Shift.ShiftTime;

class Employee{
   private String name;
   private int id;
   private BankDetails bankAccount;
   private int salary;
   private List<EmploymentConditions> empConditions;
   private Date employmentDate;
   private Constraints workingConstraints;
   private boolean isFired;

   private class Constraints {
      //private List<Integer> unavailableWeekDays;
      private List<Role> availableRoles;
      private List<Branch> availableBranches;
      private List<Date> unavailableDates;
      private final int MAX_WORKDAYS_A_WEEK = 6;
      private final int MAX_SHIFTS_A_DAY = 1;
      private Employee employee;
  
      public Constraints(Employee emp){
          //unavailableWeekDays = new LinkedList<Integer>();
          availableRoles = new LinkedList<Role>();
          availableRoles.add(Role.GENERAL_WORKER);
          availableBranches = Branch.getAllBranches();
          this.employee = emp;
          this.unavailableDates = new LinkedList<Date>();
      }
  
      public boolean checkScheduleLegality(Date from, Date until){
          List<Branch> branches = Branch.getAllBranches();
          int workDaysAWeek = 0;
          int shiftsADay = 0;
  
          Date current = from;
          while(current.betweenDates(from, until)){
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
              if(shiftsADay>this.MAX_SHIFTS_A_DAY)
                  return false;// more shifts a day than possible
              shiftsADay = 0;
              if(current.getWeekday() == 7){
                  if(workDaysAWeek>this.MAX_WORKDAYS_A_WEEK)
                      return false; //too many work days a week
                  workDaysAWeek = 0;
              }
              current = current.getNextDay();
          }
          if(workDaysAWeek>this.MAX_WORKDAYS_A_WEEK)
              return false; //too many work days a week
          return true;
      }
  
      public void addUnavailableDate(Date d){
          this.unavailableDates.add(d);
      }
  
      public void setAvailableDate(Date d) {
          this.unavailableDates.remove(d);
      }
  }

  

   public Employee(String name, int id, BankDetails bd, List<EmploymentConditions> ec, Date employmentDate){
      this.name = name;
      this.id = id;
      this.bankAccount = bd;
      this.calculateSalary();
      this.empConditions = ec;
      this.employmentDate = employmentDate;
      this.workingConstraints = new Constraints(this);
      this.isFired = false;
   }

   public int getId() {
       return this.id;
   }

   public boolean getIsFired(){
      return isFired;
   }

   public void addUnavailableDate(Date d){
      this.workingConstraints.addUnavailableDate(d);
  }

   public void setAvailableDate(Date d){
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
        if(!this.workingConstraints.checkScheduleLegality(Date.getCurrentDate(), Date.getInstance(Date.getCurrentDate().getYear()+1, Date.getCurrentDate().getMonth(), Date.getCurrentDate().getDay())))
            legalityWarning = "WARNING! shift does'nt meet constraints!";
      return "UNFINISHED";
   }
   public boolean checkLegality(Date a, Date b){
      return this.workingConstraints.checkScheduleLegality(a,b);
   }

   public void calculateSalaryAutomatically() throws Exception{
      throw new Exception("unimplented");
   }

   public void setIsFired(boolean is){
      this.isFired = is;
   }
}