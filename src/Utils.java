import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String FORMAT_TIME = "HH:mm:ss";

    public static Boolean isValidTime(String time) {
        final String regex = "^(2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$";
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);

        return matcher.matches();
    }

    public static long parseDateToMillis(String date) {
        long millis = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat(FORMAT_TIME);
            Date dateParsed = format.parse(date);
            millis = dateParsed.getTime();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return millis;
    }

    public static String parseMillisToDate(long time) {
        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat(FORMAT_TIME);
        return df.format(currentDate);
    }

}
