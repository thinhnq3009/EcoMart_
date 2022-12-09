package eco.app.model;

import java.util.Calendar;
import java.util.Date;

import eco.app.myswing.LabelDateChooser;

/**
 *
 * @author ThinhNQ
 */
public class DateChooseGroup {

    /**
     * It sets the date range of a date chooser
     * 
     * @param from The LabelDateChooser object that will be set to the start date.
     * @param to   The date chooser that will be set to the end date.
     */
    public static void setPeriod(LabelDateChooser from, LabelDateChooser to, int... periods) {

        if (periods.length == 0) {
            return;
        }

        int period = periods[0];

        System.out.println(period);
        if (period == 0) {
            from.setSelectedDate(new Date());
            to.setSelectedDate(new Date());
        } else if (period == -999999) {

            Date fromDate = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.set(Calendar.YEAR, 2000);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            fromDate = cal.getTime();

            from.setSelectedDate(fromDate);
        } else {
            if (period > 0) {

                Date toDate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(toDate);
                cal.add(Calendar.DAY_OF_MONTH, period);
                toDate = cal.getTime();

                from.setSelectedDate(new Date());
                to.setSelectedDate(toDate);

            } else {
                Date fromDate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(fromDate);
                cal.add(Calendar.DAY_OF_MONTH, period);
                fromDate = cal.getTime();

                from.setSelectedDate(fromDate);

            }
        }

        from.changeText();

        to.changeText();

    }

    /**
     * If the from date is greater than the to date, swap the dates.
     * 
     * @param from The LabelDateChooser that will be paired with the to
     *             LabelDateChooser.
     * @param to   The LabelDateChooser that will be paired with the from
     *             LabelDateChooser.
     */
    public static void pairing(LabelDateChooser from, LabelDateChooser to) {
        from.addEvent((sd) -> {
            Date fromDate = from.getSelectedDate();
            Date toDate = to.getSelectedDate();
            if (fromDate.compareTo(toDate) > 0) {
                to.setSelectedDate(fromDate);
                from.setSelectedDate(toDate);

                from.changeText();
                to.changeText();
            }
        });

        to.addEvent((sd) -> {
            Date fromDate = from.getSelectedDate();
            Date toDate = to.getSelectedDate();
            if (fromDate.compareTo(toDate) > 0) {
                from.setSelectedDate(toDate);
                to.setSelectedDate(fromDate);

                from.changeText();
                to.changeText();
            }
        });
    }

}
