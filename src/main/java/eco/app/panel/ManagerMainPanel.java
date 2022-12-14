/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package eco.app.panel;

import eco.app.component.Container;
import eco.app.component.Header;
import eco.app.component.Menu;
import eco.app.component.MenuItem;
import eco.app.dialog.Login;
import eco.app.helper.ShareData;
import eco.app.model.MenuModel;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author Lenovo
 */
public class ManagerMainPanel extends javax.swing.JPanel {

    private MigLayout layout;
    private Header header;
    private Menu menu;
    private Container container;
    private Animator animator;
    private boolean close = false;
    private final int W_CLOSE = 61;
    private final int W_OPEN = 205;

    private final MenuItem customersItem = new MenuItem(new MenuModel("Customers", "customer.png"));
    private final MenuItem productsItem = new MenuItem(new MenuModel("Products", "product.png"));
    private final MenuItem sellingItem = new MenuItem(new MenuModel("Selling", "order.png"));
    private final MenuItem orderItem = new MenuItem(new MenuModel("Order", "order.png"));
    private final MenuItem logoutItem = new MenuItem(new MenuModel("Logout", "logout.png"));
    private final MenuItem voucherItem = new MenuItem(new MenuModel("Voucher", "voucher.png"));
    private final MenuItem employeeItem = new MenuItem(new MenuModel("Employee", "employee.png"));
    private final MenuItem statisticItem = new MenuItem(new MenuModel("Statistic", "statistic.png"));

    public ManagerMainPanel() {
        initComponents();
        init();

    }

    public void updateInfo() {
        header.updateInfo();
    }

    private void init() {

        // Initialize the parts of the app
        menu = new Menu();
        header = new Header();
        container = new Container();

        // Start Layout
        layout = new MigLayout("fill", "0[]0[fill]0", "0[]0[]0");
        this.setLayout(layout);
        this.add(menu, "w 205!,h 100%, spany 2");
        this.add(header, "w 100%,h 60!, wrap");
        this.add(container, "w 100%, h 100%");

        // Create Animator
        initAnimator();

        employeeItem.setOnlyAdmin(true);
        statisticItem.setOnlyAdmin(true);

        // Add sub -menu
        addMenu(customersItem);
        addMenu(productsItem);
        addMenu(sellingItem);
        addMenu(voucherItem);
        addMenu(employeeItem);
        addMenu(orderItem);
        addMenu(statisticItem);
        addMenu(logoutItem);

        // Tạo sự kiện cho menu con
        initMenuItemEvent();

    }
    
    
    protected void addMenu(MenuItem menuItem) {
        menu.addMenuItem(menuItem);
    }

    public void initMenuItemEvent() {

        productsItem.addEvent(() -> container.changeComponent(new ProductPanel()));

        customersItem.addEvent(() -> container.changeComponent(new CustomerPanel()));

        sellingItem.addEvent(() -> container.changeComponent(new SellingPanel()));

        voucherItem.addEvent(() -> container.changeComponent(new VoucherPanel()));

        orderItem.addEvent(() -> container.changeComponent(new OrderPanel()));

        employeeItem.addEvent(() -> container.changeComponent(new EmployeePanel()));

        statisticItem.addEvent(() -> container.changeComponent(new StatisticPanel()));

        logoutItem.addEvent(() -> logout());

    }

    private void logout() {
        Login login = new Login(null, true);

        ShareData.USER_LOGIN = null;

        Component parent = SwingUtilities.getWindowAncestor(this);

        parent.setVisible(false);

        while (ShareData.USER_LOGIN == null) {
            login.setVisible(true);
        }

        parent.setVisible(true);
    }

    private void initAnimator() {

        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {

                // Menu width
                int w;

                // Calculate the width of the menu.
                if (!close) {
                    w = (int) (205 - ((W_OPEN - W_CLOSE) * fraction));
                } else {
                    w = (int) (W_CLOSE + ((W_OPEN - W_CLOSE) * fraction));
                }

                layout.setComponentConstraints(menu, "w " + w + "!,h 100%, spany 2");
                revalidate();
                repaint();
            }

            @Override
            public void end() {
                close = !close;
            }

        };

        animator = new Animator(200, target);
        animator.setResolution(0);
        animator.setDeceleration(0.5f);

        // Adding a mouse listener to the button close.
        header.getBtnShow().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                animator.start();
            }

        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(400, 300));
        setPreferredSize(new java.awt.Dimension(400, 300));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
