package eco.app.event;

import java.util.Date;

/**
 *
 * @author Lenovo
 */
public interface LabelDateChooserAction {

    /**
     * This method will be run after <code>LabelDateChooser</code> been selected
     * @param selectedDate
     */
    public void acction(Date selectedDate);
}
