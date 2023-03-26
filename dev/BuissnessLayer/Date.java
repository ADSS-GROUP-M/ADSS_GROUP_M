package dev.BuissnessLayer;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class Date {
    private final int YEAR;
    private final int MONTH;
    private final int DAY;

    private static List<Date> allInstances;

    private static Calendar calendar;

    private Date(int year, int month, int day)
    {
        this.YEAR = year;
        this.MONTH = month;
        this.DAY = day;
        if(calendar == null)
            calendar = Calendar.getInstance();
        
    }
    public static Date getInstance(int year, int month, int day)
    {
        if(allInstances == null)
            allInstances = new LinkedList<Date>();

        for (Date d: allInstances){
            if(d.YEAR == year && d.MONTH == month && d.DAY == day)
                return d;
        }
        Date d = new Date(year,month,day);
        allInstances.add(d);
        return d;
    }

    public int getWeekday(){
        Calendar c = Calendar.getInstance();
        c.set(this.YEAR,this.MONTH,this.DAY);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public boolean betweenDates(Date from, Date until){ //including edges, should unit test
        boolean after = false;
        boolean before = false;
        Calendar c = Calendar.getInstance();
        Calendar compare = Calendar.getInstance();
        c.set(this.YEAR,this.MONTH,this.DAY);
        compare.set(until.YEAR,until.MONTH,until.DAY+1,0,0);
        before = c.before(compare);
        compare.set(from.YEAR,from.MONTH,from.DAY-1,23,59,59);
        after = c.after(compare);
        return before && after;
    }

    public Date getNextDay(){ //considers that dates 29.2 - 31.2 exist
        Calendar c = Calendar.getInstance();
        c.set(this.YEAR,this.MONTH,this.DAY);
        if(c.get(Calendar.DATE) == 30 && (c.get(Calendar.MONTH) == 4 || c.get(Calendar.MONTH) == 6 || c.get(Calendar.MONTH) == 9 || c.get(Calendar.MONTH) == 11))
            return getInstance(this.YEAR, this.MONTH+1, 1);
        else if (c.get(Calendar.DATE) == 31)
            return getInstance(this.YEAR, this.MONTH+1, 1);
        else
            return getInstance(this.YEAR, this.MONTH, this.DAY+1);
    }

    public static Date getCurrentDate(){
        Calendar c = calendar;
        return Date.getInstance(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.YEAR));
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
    /* 
    public static Date[] getWeekDates(Date d){ 
        Date[] fromUntil = new Date[2];
        Calendar c = Calendar.getInstance();
        int x = c.get(Calendar.DAY_OF_WEEK);
        Date.getInstance(d.YEAR, d.MONTH, x)
    }*/
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
