package eco.app.myswing;

import eco.app.event.LabelDateChooserAction;
import eco.app.helper.Convertor;
import eco.app.swing.chooser.date.DateChooser;
import eco.app.swing.chooser.date.SelectedAction;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ThinhNQ
 */
public class LabelDateChooser extends Link {

    Date selectedDate = new Date();
    private List<LabelDateChooserAction> actions = new ArrayList<>();
    private DefaultTime defaultTime = DefaultTime.START_OF_DAY;

    public enum DefaultTime {
        START_OF_DAY, END_OF_DAY
    }

    public LabelDateChooser() {

        this.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eco/app/icon/icons8_calendar_20px_1.png"))); // NOI18N

        this.setText("dd/MM/yyyy");

        this.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N

        this.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        this.setFocusable(false);

        changeText();

        initEvent();

    }

    /**
     * > When the mouse is clicked, a DateChooser is created, and if the
     * selectedDate is not null, the
     * DateChooser is set to the selectedDate. Then, the DateChooser is added to the
     * event, and if the
     * action is DAY_SELECTED, the selectedDate is set to the selectedDate, the
     * DateChooser is hidden,
     * the text is changed, and the event is run. Finally, the DateChooser is shown
     * as a popup
     */
    private void initEvent() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DateChooser chooser = new DateChooser();

                if (selectedDate != null) {
                    chooser.setSelectedDate(selectedDate);
                }

                chooser.addEventDateChooser((action, sd) -> {
                    if (action.getAction() == SelectedAction.DAY_SELECTED) {
                        selectedDate = Convertor.readSelectedDate(sd);
                        chooser.hidePopup();
                        changeText();
                        runEvent();

                    }
                });

                chooser.showPopup(LabelDateChooser.this, e.getX(), e.getY());

            }
        });
    }

    // Change time in selectedDate to 00:00:00 or 23:59:59
    private void changeTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        if (defaultTime == DefaultTime.START_OF_DAY) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        }

        selectedDate = calendar.getTime();
    }

    public DefaultTime getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(DefaultTime defaultTime) {
        this.defaultTime = defaultTime;
    }

    public void changeText() {
        this.setText(Convertor.dateToString(selectedDate));
    }

    public void addEvent(LabelDateChooserAction action) {
        actions.add(action);
    }

    public void runEvent() {
        for (LabelDateChooserAction action : actions) {
            action.acction(selectedDate);
        }
    }

    public Date getSelectedDate() {
        changeTime();
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

}
