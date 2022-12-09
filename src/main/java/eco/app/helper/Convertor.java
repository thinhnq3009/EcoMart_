package eco.app.helper;

import eco.app.swing.chooser.date.SelectedDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ThinhNQ
 */
public class Convertor {

    public static SimpleDateFormat formatterDate = new SimpleDateFormat(SaveData.PATTERN_DATE);
    public static SimpleDateFormat formatterTime = new SimpleDateFormat(SaveData.PATTERS_TIME);
    public static SimpleDateFormat formatterDateTime = new SimpleDateFormat(SaveData.PATTERS_DATETIME);

    public static String dateToString(Date date) {
        return formatterDate.format(date);
    }

    public static Date plusDate(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(field, amount);

        return calendar.getTime();

    }

    public static Date today() {
        return new Date();
    }

    public static String todayString() {
        return dateToString(today());
    }

    public static Date stringToDate(String str) throws Exception {
        try {
            return formatterDate.parse(str);
        } catch (ParseException ex) {
            throw new Exception("This string can't convert to Date");
        }
    }

    public static String dateTimeToString(Date date) {
        return formatterTime.format(date);
    }

    /**
     * FORMAT STRING
     *
     * @param str 1 chuỗi các số
     * @return
     */
    public static String formatCurrency(String str) {
        return str.replaceAll("(\\d)(?=(\\d{3})+(?!\\d))", "$1.");
    }

    public static String formatCurrency(int c) {
        String str = c + "";
        return str.replaceAll("(\\d)(?=(\\d{3})+(?!\\d))", "$1.");
    }

    public static void formatCurrency(Object[] objects, int... indexes) {
        for (int index : indexes) {
            Object o = objects[index];

            objects[index] = formatCurrency(o);

        }
    }

    public static String formatCurrency(Object o) {
        if (o instanceof String s) {
            if (s.matches("[0-9]+")) {
                return formatCurrency(s);
            } else {
                throw new RuntimeException(o + " is not a number");
            }
        } else if (o instanceof Integer i) {
            return formatCurrency((int) i);
        } else {
            throw new RuntimeException(o + ". This element is not converted !");
        }
    }

    /**
     * Get day mouth year from SelectedDate return Date by this data
     *
     * @param sd
     * @return
     */
    public static Date readSelectedDate(SelectedDate sd) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(sd.getYear(), sd.getMonth() - 1, sd.getDay());

        return calendar.getTime();

    }

    public static String formatDate(Object obj) {
        if (obj instanceof Date date) {
            return formatterDate.format(date);
        } else {
            return obj.toString();
        }
    }

    public static String formatTime(Object obj) {
        if (obj instanceof Date date) {
            return formatterTime.format(date);
        } else {
            return obj.toString();
        }
    }

    public static String formatDateTime(Object obj) {
        if (obj instanceof Date date) {
            return formatterDateTime.format(date);
        } else {
            return obj.toString();
        }
    }

}
