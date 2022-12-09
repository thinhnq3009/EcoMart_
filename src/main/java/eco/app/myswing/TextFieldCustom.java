/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.myswing;

import eco.app.event.ValidateActionAdapter;
import eco.app.helper.SaveData;
import eco.app.swing.effect.RippleEffect;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author Lenovo
 */
public class TextFieldCustom extends JTextField {

    // Text field effect
    private boolean mouseIn = false;
    private Animator animator;
    private float fraction;
    private boolean isFocus = false;
    private RippleEffect rippleEffect = new RippleEffect(this);
    private Color lineColor = SaveData.TXT_UNDER_LINE;
    // Text field validate data
    private String reg;
    private List<ValidateActionAdapter> validateActions;
    private boolean canEmpty = true;

    public TextFieldCustom() {
        this.reg = "[^.]+"; // default reg
        validateActions = new ArrayList<>();

        setBorder(new EmptyBorder(15, 10, 10, 5));

        hoverEffect();
        focusEffect();

        rippleEffect.setRippleColor(SaveData.TXT_RIPPLE_EFFECT);

    }

    @Override
    public void setText(String t) {
        runValid();
        super.setText(t);
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
        g2.setColor(getLineColor());
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
        // Hover effect
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

    private void start() throws Exception {
        animator.start();
        throw new Exception("...");
    }

    private void focusEffect() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {

                isFocus = true;
                if (!mouseIn) {
                    try {
                        start();
                    } catch (Exception ex) {
                    }
                }
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocus = false;
                repaint();

                check();
            }

        });

    }

    public void runValid() {
        for (ValidateActionAdapter validateAction : validateActions) {
            validateAction.validAction(this);
        }
    }

    public void runInvalid() {
        for (ValidateActionAdapter validateAction : validateActions) {
            validateAction.invalidAction(this);
        }
    }

    public String getRegex() {
        return reg;
    }

    public void setConstrain(String regex, boolean canEmpty) {
        setRegex(regex);
        setCanEmpty(canEmpty);
    }

    public void setRegex(String reg) {
        this.reg = reg;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setValidateAction(ValidateActionAdapter validateAction) {
        this.validateActions.add(validateAction);
    }

    /*
     * Getter and setter
     */
    public boolean isCanEmpty() {
        return canEmpty;
    }

    public void setCanEmpty(boolean canEmpty) {
        this.canEmpty = canEmpty;
    }

    public RippleEffect getRippleEffect() {
        return rippleEffect;
    }

    public void setRippleEffect(RippleEffect rippleEffect) {
        this.rippleEffect = rippleEffect;
    }

    private boolean validateText() {
        String text = this.getText();
        return canEmpty
                ? text.matches(getRegex()) || text.isBlank()
                : text.matches(getRegex()) && !text.isBlank();
    }

    public void check() {
        check(null, null);
    }

    public boolean isMouseIn() {
        return mouseIn;
    }

    public void setMouseIn(boolean mouseIn) {
        this.mouseIn = mouseIn;
    }

    public void check(StringBuilder sb, String message) {
        if (validateActions.isEmpty()) {
            return;
        }

        if (validateText()) {
            runValid();

        } else {
            if (sb != null) {
                sb.append(message);
            }
            runInvalid();
        }

    }

}
