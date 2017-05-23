import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;


/**
 * Created by Jakob on 17/05/17.
 */
public class TimeStampHelper {

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdfDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public XMLGregorianCalendar getTimeGregorian() {
        XMLGregorianCalendar gregFmt = null;
        try {
            gregFmt = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return gregFmt;
    }
}