package voiceapp.db;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class used to convert date string to Timestamp.
 */
public class DateParser {
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static Timestamp getDate(String dateToConvert) {
        try {
            return new Timestamp(new SimpleDateFormat(DATE_FORMAT).parse(dateToConvert).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
