package eco.app.panel;

import eco.app.component.MenuItem;

/**
 *
 * @author ThinhNQ
 */
public class EmployeeMainPanel extends ManagerMainPanel {
    
    @Override
    protected void addMenu(MenuItem menuItem) {
        
        if (menuItem.isOnlyAdmin()) {
            return;
        }
        
        super.addMenu(menuItem);
        
    }
    
}
