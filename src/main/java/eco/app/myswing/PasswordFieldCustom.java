/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.myswing;

import eco.app.helper.SaveData;
import eco.app.swing.effect.RippleEffect;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author Lenovo
 */
public class PasswordFieldCustom extends JPasswordField {

    private boolean mouseIn = false;
    private Animator animator;
    private float fraction;
    private boolean isFocus = false;
    private RippleEffect rippleEffect = new RippleEffect(this);

    public PasswordFieldCustom() {
        setBorder(new EmptyBorder(15, 10, 10, 5));
        
        hoverEffect();
        focusEffect();

        rippleEffect.setRippleColor(SaveData.TXT_RIPPLE_EFFECT);

    }

    public RippleEffect getRippleEffect() {
        return rippleEffect;
    }

    public void setRippleEffect(RippleEffect rippleEffect) {
        this.rippleEffect = rippleEffect;
    }

    public boolean isMouseIn() {
        return mouseIn;
    }

    public void setMouseIn(boolean mouseIn) {
        this.mouseIn = mouseIn;
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);

        
        createHoverLine(g2);

        getRippleEffect().reder(g, new Area(new Rectangle2D.Double(0, 0, getWidth(), getHeight())));

        g2.dispose();

    }

    private void createHoverLine(Graphics2D g2) {

        int w = getWidth();
        int h = getHeight();
        g2.setColor(SaveData.TXT_UNDER_LINE);
        double size = 0;

        
        
        if ((!animator.isRunning() && mouseIn)
                || (!animator.isRunning() && isFocus)) {
            size = w;
        } else if ((animator.isRunning() && mouseIn)
                || (animator.isRunning() && isFocus && !mouseIn)) {
            size = (double) (w * fraction);
        } else if ((animator.isRunning() && !mouseIn && !isFocus)) {
            size = (double) (w * (1 - fraction));
        }

        double x = (w - size) / 2;
        g2.fillRect((int) (x), h - 2, (int) size, 2);
    }

    
    private void hoverEffect() {

        // Hiệu ứng hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setMouseIn(true);
                if (animator.isRunning()) {
                    animator.stop();
                }
                animator.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setMouseIn(false);
                if (animator.isRunning()) {
                    animator.stop();
                }
                animator.start();

            }

        });
        TimingTarget target = new TimingTargetAdapter() {

            @Override
            public void timingEvent(float f) {
                fraction = f;
                repaint();
            }

            @Override
            public void end() {
            }

        };
        animator = new Animator(250, target);
        animator.setResolution(5);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
    }

    private void focusEffect() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {

                isFocus = true;
                if (!mouseIn) {
                    animator.start();
                }
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocus = false;
                repaint();
            }

        });

    }

}
