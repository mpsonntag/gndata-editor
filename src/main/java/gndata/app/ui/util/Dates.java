package gndata.app.ui.util;


import java.time.*;
import java.util.Calendar;


public class Dates {

    public static Calendar toCalendar(LocalDate dt) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, dt.getYear());
        cal.set(Calendar.MONTH, dt.getMonthValue() - 1);
        cal.set(Calendar.DAY_OF_MONTH, dt.getDayOfMonth());

        return cal;
    }

    public static Calendar toCalendar(LocalDateTime dt) {
        Calendar cal = Dates.toCalendar(dt.toLocalDate());

        cal.set(Calendar.HOUR_OF_DAY, dt.getHour());
        cal.set(Calendar.MINUTE, dt.getMinute());

        return cal;
    }
}
