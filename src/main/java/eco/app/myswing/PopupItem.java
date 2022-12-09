package eco.app.myswing;

import javax.swing.JMenuItem;

/**
 *
 * @author ThinhNQ
 */
public class PopupItem extends JMenuItem {

    public PopupItem() {
        super();
        init();
    }

    private void init() {

        this.setBackground(new java.awt.Color(255, 204, 204));

        this.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N

        this.setText("Delete all");

    }

}
