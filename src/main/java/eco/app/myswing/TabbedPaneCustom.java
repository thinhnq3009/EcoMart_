/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.myswing;

import eco.app.panel.ManagerMainPanel;
import eco.app.helper.SaveData;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author Lenovo
 */
public class TabbedPaneCustom extends JTabbedPane {

    public TabbedPaneCustom() {
        setUI(new CustomTabbedUI());
        setPreferredSize(new Dimension(100, 100));
    }

    public class CustomTabbedUI extends MetalTabbedPaneUI {

        private Animator animator;
        private Area area;
        private TimingTarget target;
        private int oldIndex = 0;
        private int curIndex;
        private BufferedImage effect;
        private Color colorSelected = new Color(255, 255, 255);
        private Color tabColor = SaveData.TABBED;
        private int duration = 500;

        public CustomTabbedUI() {

        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public Color getColorSelected() {
            return colorSelected;
        }

        public void setColorSelected(Color colorSelected) {
            this.colorSelected = colorSelected;
        }

        public Color getTabColor() {
            return tabColor;
        }

        public void setTabColor(Color tabColor) {
            this.tabColor = tabColor;
        }

        // Tạo roundRectangel trong lúc chuyển tab
        private void createEffect(float f) {
            int wOld = getBoundsAt(oldIndex).width;
            int wNew = getBoundsAt(curIndex).width;
            int xOld = getBoundsAt(oldIndex).x;
            int xNew = getBoundsAt(curIndex).x;
            int h = getBoundsAt(curIndex).height;
            int yOld = getBoundsAt(oldIndex).y;
            int yNew = getBoundsAt(curIndex).y;

            int w = 0;
            int x = 0;
            int y = 0;

            x = (int) (xOld + (xNew - xOld) * f);
            w = (int) (wOld + (wNew - wOld) * f);
            y = (int) (yOld + (yNew - yOld) * f);

            area = new Area(new RoundRectangle2D.Double(x, y, w, h, h, h));

            effect = new BufferedImage(x + w, h, BufferedImage.TYPE_4BYTE_ABGR);

            Graphics2D g2 = effect.createGraphics();
            g2.setColor(getColorSelected());
            g2.fill(area);
            g2.dispose();

        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);

            tabPane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent ce) {
                    target = new TimingTargetAdapter() {
                        @Override
                        public void begin() {
                            curIndex = getSelectedIndex();
                        }

                        @Override
                        public void timingEvent(float fraction) {
                            createEffect(fraction);
                            repaint();
                        }

                        @Override
                        public void end() {
                            oldIndex = getSelectedIndex();
                        }

                    };
                    animator = new Animator(duration, target);
                    animator.start();

                }
            });
            tabPane.setBorder(new EmptyBorder(0, 0, 15, 0));

        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            if (isOpaque()) {
                super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            }
        }

        //Xoá boder trong
        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        }

        // Paint menu
        @Override
        protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
            Rectangle rect = rects[tabIndex];
            Area menuArea = new Area();
            int w = rect.width;
            int h = rect.height;
            int x = rect.x;
            int y = rect.y;

            if (area == null) {
                area = new Area();
            }

            if (tabIndex == 0 && rects.length == 1) {
                RoundRectangle2D round = new RoundRectangle2D.Double(x, y, w, h, h, h);
                area.add(new Area(round));
                // Fisrt tab 
            } else if (tabIndex == 0) {
                RoundRectangle2D round = new RoundRectangle2D.Double(x, y, w, h, h, h);
                menuArea.add(new Area(round));
                menuArea.add(new Area(new Rectangle(x + h, y, w - h, h)));
                // Last tab
            } else if (tabIndex == rects.length - 1) {
                RoundRectangle2D round = new RoundRectangle2D.Double(x, y, w, h, h, h);
                menuArea.add(new Area(round));
                menuArea.add(new Area(new Rectangle(x, y, w - h, h)));

            } else {
                RoundRectangle2D round = new RoundRectangle2D.Double(x, y, w, h, 0, 0);
                menuArea.add(new Area(round));

            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getTabColor());

            g2.fill(menuArea);

            g2.dispose();

            super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
        }

        // Paint selected
        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(255, 255, 255));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (area == null || !animator.isRunning()) {
                if (isSelected) {
                    area = new Area(new RoundRectangle2D.Double(x, y, w, h, h, h));
                }
            }

            if (animator.isRunning()) {

                g2.drawImage(effect, 0, 0, null);
            }
            if (area != null) {
//                if (tabPlacement == TOP) {
                g2.fill(area);
//                }
            }
            g2.dispose();
        }

    }

}
