package dev.BuissnessLayer;

import java.util.LinkedList;
import java.util.List;

public enum Branch {
    BRANCH1("Branch1"),
    BRANCH2("Branch2"),
    BRANCH3("Branch3"),
    BRANCH4("Branch4"),
    BRANCH5("Branch5"),
    BRANCH6("Branch6"),
    BRANCH7("Branch7"),
    BRANCH8("Branch8"),
    BRANCH9("Branch9");

    private String name;
    private List<Date>  nonWorkingDays;

    private Branch(String name){
        this.name = name;
        this.nonWorkingDays = new LinkedList<Date>();
    }

    public void addNonWorkingDay(Date d){
        this.nonWorkingDays.add(d);
    }

    public boolean isWorkingDay(Date date){
        for(Date d : this.nonWorkingDays){
            if(d == date)
                return false;
        }
        return true;
    }

    public static List<Branch> getAllBranches(){
        List<Branch> b = new LinkedList<Branch>();
        for( Branch br : Branch.values()){
            b.add(br);
        }
        return b;
    }
}
