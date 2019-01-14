package sem.ru.plusminus.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {

    private static SimpleDateFormat format =
            new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss");

    public static String getCurrentTime(){
        return format.format(Calendar.getInstance().getTime());
    }
}
