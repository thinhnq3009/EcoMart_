/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.myswing;

import eco.app.helper.SaveData;
import eco.app.swing.effect.RippleEffect;
import eco.app.swing.effect.ShadowRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Lenovo
 */
public class ButtonRandius extends JButton {

    private int round = 10;

    private Color shadowColor = new Color(51, 51, 51);
    private BufferedImage imageShadow;
    private Insets shadowSize = new Insets(2, 5, 8, 5);
    private RippleEffect rippleEffect = new RippleEffect(this);

    public ButtonRandius() {
        init();
    }
    
    public ButtonRandius(String text) {
        init();
        setText(text);
    }
    
    
    private void init() {
        setBorder(new EmptyBorder(10, 12, 15, 12));
        setContentAreaFilled(false);
        setBackground(Color.WHITE);
        setForeground(new Color(51, 51, 51));
        rippleEffect.setRippleColor(SaveData.BTN_RIPPLE_EFFECT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        setFocusable(false);
    }
    
    

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        createImageShadow();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double width = getWidth() - (shadowSize.left + shadowSize.right);
        double height = getHeight() - (shadowSize.top + shadowSize.bottom);
        double x = shadowSize.left;
        double y = shadowSize.top;
        //  Create Shadow Image
        g2.drawImage(imageShadow, 0, 0, null);
        //  Create Background Color
        g2.setColor(getBackground());
        Area area = new Area(new RoundRectangle2D.Double(x, y, width, height, getRound(), getRound()));
        g2.fill(area);
        rippleEffect.reder(grphcs, area);
        g2.dispose();
        super.paintComponent(grphcs);
    }

    public void createImageShadow() {
        int h = getHeight();
        int w = getWidth();
        if (w > 0 && h > 0) {
            imageShadow = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = imageShadow.createGraphics();
            g2.drawImage(createShadow(), 0, 0, null);
            g2.dispose();
        }
    }

    private BufferedImage createShadow() {
        int width = getWidth() - (shadowSize.left + shadowSize.right);
        int height = getHeight() - (shadowSize.top + shadowSize.bottom);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fill(new RoundRectangle2D.Double(0, 0, width, height, getRound(), getRound()));
        g2.dispose();

        return new ShadowRenderer(5, 0.3f, getShadowColor()).createShadow(image);

    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        createImageShadow();
        repaint();
    }

    public void setOvalRound() {
        setRound(this.getHeight());
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        createImageShadow();
        repaint();
    }

    public Color getRippleEffectColor() {
        return rippleEffect.getRippleColor();
    }

    public void setRippleEffectColor(Color color) {
        this.rippleEffect.setRippleColor(color);
    }

}
