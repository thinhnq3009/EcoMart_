package eco.app.panel;

import eco.app.entity.Product;
import eco.app.model.DateChooseGroup;

/**
 *
 * @author Lenovo
 */
public class StatisticPanel extends javax.swing.JPanel {

    private DateChooseGroup chooseGroup;

    /**
     * Creates new form StatisticPanel
     */
    public StatisticPanel() {
        initComponents();

        init();

    }

    private void init() {
        tabbedPane.add(new StatisticProductPanel(), "Product", 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new eco.app.myswing.TabbedPaneCustom();

        setOpaque(false);

        tabbedPane.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private eco.app.myswing.TabbedPaneCustom tabbedPane;
    // End of variables declaration//GEN-END:variables


}
