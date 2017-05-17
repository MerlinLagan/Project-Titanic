import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jakob on 17/05/17.
 */
public class TimeStampHelper {
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}