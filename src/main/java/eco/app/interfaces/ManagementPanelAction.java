package eco.app.interfaces;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lenovo
 * @param <E> type of Entity this panel manages
 * 
 * 
 * 
 */
public interface ManagementPanelAction<E> {
    
    public void fillTable();
    
    public void addTableRow(E entity);

    public boolean validateForm(StringBuilder sb);

    public void clearForm();

    public void fillForm(E entity);

    public E readForm(StringBuilder sb) throws Exception;

    
}
