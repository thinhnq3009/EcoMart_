
package eco.app.myswing;

import eco.app.swing.model.ModernScrollBarUI;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ScrollBarCustom extends JScrollBar {

    public ScrollBarCustom() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(5, 5));
        setForeground(new Color(0, 0, 204, 255));
        setUnitIncrement(40);
        setOpaque(true);
    }
}