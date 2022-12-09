package eco.app.myswing;

import eco.app.swing.chooser.api.JnaFileChooser;
import java.awt.Component;
import java.awt.Window;
import javax.swing.SwingUtilities;

/**
 *
 * @author ThinhNQ
 */
public class MyChooser extends JnaFileChooser {

    public MyChooser() {
        super();
    }

    public boolean showOpenDialog(Component component) {
        Window window = SwingUtilities.getWindowAncestor(component);
        return super.showOpenDialog(window);
    }

    
     public boolean showSaveDialog(Component component) {
        Window window = SwingUtilities.getWindowAncestor(component);
        return super.showSaveDialog(window);
    }
}
