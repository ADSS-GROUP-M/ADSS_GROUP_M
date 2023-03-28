package dev.BusinessLayer.Employees;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Date {
    private final int YEAR;
    private final int MONTH;
    private final int DAY;

    private static List<LocalDate> allInstances;

    private static Calendar calendar;

    private Date(int year, int month, int day)
    {
        this.YEAR = year;
        this.MONTH = month;
        this.DAY = day;
        if(calendar == null)
            calendar = Calendar.getInstance();
        
    }
    public static LocalDate getInstance(LocalDate date)
    {
        if(allInstances == null)
            allInstances = new ArrayList<>();

        for (LocalDate d: allInstances){
            if(d.getYear() == date.getYear() && d.getMonth() == date.getMonth() && d.getDayOfWeek() == date.getDayOfWeek())
                return d;
        }
        allInstances.add(date);
        return date;
    }

    public int getWeekday(){
        Calendar c = Calendar.getInstance();
        c.set(this.YEAR,this.MONTH,this.DAY);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public boolean betweenDates(LocalDate from, LocalDate until) { //including edges, should unit test
        LocalDate current = LocalDate.of(YEAR,MONTH,DAY);
        return current.equals(from) || current.equals(until) || (current.isAfter(from) && current.isAfter(until));
        //boolean after = false;
        //boolean before = false;
        //Calendar c = Calendar.getInstance();
        //Calendar compare = Calendar.getInstance();
        //c.set(this.YEAR,this.MONTH,this.DAY);
        //compare.set(until.YEAR,until.MONTH,until.DAY+1,0,0);
        //before = c.before(compare);
        //compare.set(from.YEAR,from.MONTH,from.DAY-1,23,59,59);
        //after = c.after(compare);
        //return before && after;
    }

    //public Date getNextDay(){ //considers that dates 29.2 - 31.2 exist
    //    Calendar c = Calendar.getInstance();
    //    c.set(this.YEAR,this.MONTH,this.DAY);
    //    if(c.get(Calendar.DATE) == 30 && (c.get(Calendar.MONTH) == 4 || c.get(Calendar.MONTH) == 6 || c.get(Calendar.MONTH) == 9 || c.get(Calendar.MONTH) == 11))
    //        return getInstance(this.YEAR, this.MONTH+1, 1);
    //    else if (c.get(Calendar.DATE) == 31)
    //        return getInstance(this.YEAR, this.MONTH+1, 1);
    //    else
    //        return getInstance(this.YEAR, this.MONTH, this.DAY+1);
    //}

    public static LocalDate getCurrentDate(){
        Calendar c = calendar;
        return Date.getInstance(LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.YEAR)));
    }
    public static Integer[] getCurrentTime(){
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer minute = calendar.get(Calendar.MINUTE);
        Integer[] arr = {hour,minute};
        return arr;
    }
    public static  void setSystemTime(int year, int month, int day, int hour, int minute){
        calendar.set(year, month, day, hour, minute);
    }

    public static LocalDate[] getWeekDates(LocalDate d){
        LocalDate[] fromUntil = new LocalDate[2];
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // calculate monday week ago (moves cal 7 days back)
        cal.add(Calendar.DATE, 0);
        java.util.Date sunday = cal.getTime();

        // calculate sunday last week (moves cal 6 days fwd)
        cal.add(Calendar.DATE, 7);
        java.util.Date nextsunday = cal.getTime();
        fromUntil[0] = Date.getInstance(LocalDate.of(sunday.getYear(), sunday.getMonth()+1, sunday.getDate()));
        fromUntil[1] = Date.getInstance(LocalDate.of(nextsunday.getYear(), nextsunday.getMonth()+1, nextsunday.getDate()));
        return fromUntil;
    }
    public int getYear() {
        return YEAR;
    }
    public int getMonth(){
        return MONTH;
    }
    public int getDay(){
        return DAY;
    }
    
}
