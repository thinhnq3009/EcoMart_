/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package eco.app.component;

import eco.app.model.MenuModel;
import eco.app.dialog.MessageDialog;
import eco.app.event.MenuEvent;
import eco.app.helper.MessageHelper;
import eco.app.helper.SaveData;
import eco.app.helper.ShareData;

import javax.swing.ImageIcon;
import eco.app.swing.effect.RippleEffect;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author Lenovo
 */
public class MenuItem extends javax.swing.JPanel {

    /**
     * Creates new form MenuItem
     */
    private RippleEffect rippleEffect = new RippleEffect(this);
    private MenuModel model;
    private final List<MenuEvent> events = new ArrayList<>();
    private boolean mouseIn = false;
    private Animator animator;
    private BufferedImage coating;

    private boolean onlyAdmin = false;

    public MenuItem(MenuModel model) {
        init();
        setModel(model);
    }

    public MenuItem() {
        init();
    }

    private void init() {

        initComponents();
        setOpaque(false);
        rippleEffect.setRippleColor(new Color(231, 231, 231));

        lblText.setForeground(SaveData.TEXT);

        // create hover effect
        createHover();

        // create animator
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (mouseIn) {
                    createCoating((int) (getWidth() * fraction));
                } else {
                    createCoating((int) (getWidth() * (1 - fraction)));
                }
                repaint();
            }
        };
        animator = new Animator(200, target);

        // Adding a mouse listener to the menu item.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (!authorization()) {
                    MessageHelper.showErrorMessage(MenuItem.this, "You do not have access to this function");
                    return;
                }

                for (MenuEvent evt : events) {
                    evt.menuClick();
                }
            }
        });

    }

    private boolean authorization() {
        if (onlyAdmin) {
            return ShareData.USER_LOGIN.isIsAdministrator();
        }
        return true;
    }

    private void paintCoating(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        g2.drawImage(coating, 0, 0, null);

        g2.dispose();
    }

    private BufferedImage createCoating(int width) {

        int height = getHeight();

        coating = new BufferedImage(getWidth(), height, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2 = coating.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        g2.setColor(SaveData.MN_ITEM_HOVER_EFFECT);

        Area roundRect = new Area(new RoundRectangle2D.Double(0, 0, width, height, height, height));
        Area rect = new Area(new Rectangle2D.Double(0, 0, width > height ? height : width, height));
        roundRect.add(rect);
        g2.fill(roundRect);

        g2.dispose();
        return coating;
    }

    @Override
    public void paint(Graphics g) {

        if (!animator.isRunning() && mouseIn) {
            createCoating(getWidth());
            paintCoating(g);
        } else if (animator.isRunning()) {
            paintCoating(g);
        }
        super.paint(g);
    }

    private void createHover() {
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseIn = true;
                if (animator.isRunning()) {
                    animator.stop();
                }
                animator.start();

            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseIn = false;
                if (animator.isRunning()) {
                    animator.stop();
                }
                animator.start();

            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        int width = getWidth();
        int height = getHeight();

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, height, height));
        Area rect = new Area(new Rectangle2D.Double(0, 0, width > height ? height : width, height));
        area.add(rect);
        rippleEffect.reder(g, area);

        super.paintComponent(g);
    }

    public void addEvent(MenuEvent evt) {

        if (evt == null) {
            return;
        }

        events.add(evt);

    }

    // Apply model
    private void loadModel() {
        if (model.getText() != null) {
            setText(model.getText());
        }

        if (model.getIcon() != null) {
            lblIcon.setIcon(model.getIcon());
        }

        if (model.getColorEffect() != null) {
            rippleEffect.setRippleColor(model.getColorEffect());
        }

        if (model.getFont() != null) {
            lblText.setFont(model.getFont());
        }

        if (model.getForeground() != null) {
            lblText.setForeground(model.getForeground());
        }
    }

    public void setText(String text) {
        lblText.setText(text);
    }

    public String getText() {
        return lblText.getText();
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
        loadModel();
    }

    public boolean isOnlyAdmin() {
        return onlyAdmin;
    }

    public void setOnlyAdmin(boolean onlyAdmin) {
        this.onlyAdmin = onlyAdmin;
    }

    public Icon getIcon() {
        return lblIcon.getIcon();
    }

    public void setIcon(Icon icon) {
        this.lblIcon.setIcon(icon);
    }

    public RippleEffect getRippleEffect() {
        return rippleEffect;
    }

    public void setRippleEffect(RippleEffect rippleEffect) {
        this.rippleEffect = rippleEffect;
    }

    public void setHoverDuration(int duration) {
        this.animator.setDuration(duration);
    }

    public int getHoverDuration() {
        return animator.getDuration();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblText = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setMaximumSize(new java.awt.Dimension(200, 50));
        setMinimumSize(new java.awt.Dimension(200, 50));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(200, 50));
        setLayout(new java.awt.BorderLayout());

        lblText.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
        lblText.setForeground(new java.awt.Color(207, 245, 231));
        lblText.setText("Menu item");
        lblText.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        add(lblText, java.awt.BorderLayout.CENTER);

        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eco/app/icon/menu.png"))); // NOI18N
        lblIcon.setMaximumSize(new java.awt.Dimension(40, 40));
        lblIcon.setMinimumSize(new java.awt.Dimension(40, 40));
        lblIcon.setPreferredSize(new java.awt.Dimension(40, 40));
        add(lblIcon, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblText;
    // End of variables declaration//GEN-END:variables
}
