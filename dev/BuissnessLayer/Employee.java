package dev.BuissnessLayer;

import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

class Employee{
   private String name;
   private int id;
   private BankDetails bankAccount;
   private int salary;
   private List<EmploymentConditions> empConditions;
   private Date employmentDate;
   private Constraints workingConstraints;
   private boolean isFired;

   class Availability{
      private Map<Date,Shift> dayToShift;
      private List<Role> roles;

      private Availability(){
         dayToShift = new HashMap<Date,Shift>();
         roles = new LinkedList<Role>();
      }

      private boolean addRole(Role r){
         if(!roles.contains(r))
            roles.add(r);
         return true;
      }
      private boolean addShift(Date d, Shift s){
         Shift currentShift = dayToShift.get(d);
         if(currentShift == null)
            dayToShift.put(d,s);
         /*else if(currentShift==Shift.MORNING && s == Shift.EVENING)
         
         else if(currentShift==Shift.EVENING && s == Shift.MORNING)*/
         System.out.println("UNFINISHED METHOD Buissness - Employee - addShift");
         return true;
      }

   }

   public Employee(String name, int id, BankDetails bd, int salary, List<EmploymentConditions> ec, Date employmentDate){
      this.name = name;
      this.id = id;
      this.bankAccount = bd;
      this.salary = salary;
      this.empConditions = ec;
      this.employmentDate = employmentDate;
      this.workingConstraints = new Constraints();
      this.availability = new Availability();
      this.isFired = false;
   }

   


}