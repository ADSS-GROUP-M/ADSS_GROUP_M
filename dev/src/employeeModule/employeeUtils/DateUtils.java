package employeeModule.employeeUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.List;

public class DateUtils {
    public static final String DATE_PATTERN = "d/M/yyyy";
    public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static LocalDate[] getWeekDates(LocalDate d){
        LocalDate[] fromUntil = new LocalDate[2];
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(Date.valueOf(d));
        int dayDifference = Calendar.SUNDAY - cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_YEAR, dayDifference);
        //cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // calculate monday week ago (moves cal 7 days back)
        java.util.Date sunday = cal.getTime();

        // calculate sunday last week (moves cal 6 days fwd)
        //cal.add(Calendar.DAY_OF_MONTH, 7);
        cal.set(sunday.getYear()+1900,sunday.getMonth(),sunday.getDate()+7);
        java.util.Date nextsunday = cal.getTime();
        fromUntil[0] = LocalDate.of(sunday.getYear()+1900, sunday.getMonth()+1, sunday.getDate());
        fromUntil[1] = LocalDate.of(nextsunday.getYear()+1900, nextsunday.getMonth()+1, nextsunday.getDate());
        return fromUntil;
    }

    public static boolean validDate(String dateInput) {
        try {
            parse(dateInput);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }
    public static LocalDate[] getConsequtiveDates(LocalDate from, LocalDate until){
        List<LocalDate> x = from.datesUntil(until).toList();
        LocalDate[] dates = new LocalDate[x.size()];
        int i = 0;
        for(Object date : x ){
            dates[i] = ((LocalDate)date);
            i++;
        }
        return dates;
    }

    public static int getWeekNumber(LocalDate date) {
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    public static LocalDate parse(String dateInput) throws DateTimeParseException {
        return LocalDate.parse(dateInput,DateFormat);
    }

}
