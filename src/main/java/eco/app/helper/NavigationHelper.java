package eco.app.helper;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

/**
 *
 * @author ThinhNQ
 */
public class NavigationHelper {

    private JButton btnNew;
    private JButton btnInsert;
    private JButton btnUpdate;
    private JButton btnDelete;

    private List<Component> componentsEdit = new ArrayList<>();
    private List<Component> componentsCreate = new ArrayList<>();

    public NavigationHelper() {
    }

    public NavigationHelper(JButton btnNew, JButton btnInsert, JButton btnUpdate, JButton btnDelete) {
        this.btnNew = btnNew;
        this.btnInsert = btnInsert;
        this.btnUpdate = btnUpdate;
        this.btnDelete = btnDelete;
        init();
    }

    private void init() {
        btnNew.setEnabled(true);
        btnInsert.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        btnNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isCreateMode();
            }
        });
    }

    public void addComponentEdit(Component component) {
        componentsEdit.add(component);
    }

    public void addComponentCreate(Component component) {
        componentsCreate.add(component);
    }

    public void isCreateMode() {
        btnNew.setEnabled(true);
        btnInsert.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        for (Component component : componentsEdit) {
            component.setEnabled(false);
        }

        for (Component component : componentsCreate) {
            component.setEnabled(true);
        }

    }

    public void isEditMode() {
        btnInsert.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);

        for (Component component : componentsEdit) {
            component.setEnabled(true);
        }

        for (Component component : componentsCreate) {
            component.setEnabled(false);
        }
    }

}
