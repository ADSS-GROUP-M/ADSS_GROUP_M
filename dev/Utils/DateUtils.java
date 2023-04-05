package dev.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class DateUtils {
    public static final String DATE_PATTERN = "d/M/yyyy";
    public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern(DATE_PATTERN);

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
        fromUntil[0] = LocalDate.of(sunday.getYear(), sunday.getMonth()+1, sunday.getDate());
        fromUntil[1] = LocalDate.of(nextsunday.getYear(), nextsunday.getMonth()+1, nextsunday.getDate());
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

    public static int getWeekNumber(LocalDate date) {
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    public static LocalDate parse(String dateInput) throws DateTimeParseException {
        return LocalDate.parse(dateInput,DateFormat);
    }

}
