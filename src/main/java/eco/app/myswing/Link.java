/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.myswing;

import eco.app.event.LinkEventAdapter;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author Lenovo
 */
public class Link extends JLabel {

    private List<LinkEventAdapter> events = new ArrayList<>();

    private static final int CHANGE = 45;

    public Link() {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setBackground(new Color(0, 0, 0));
        Color colorBackgroundDefault = this.getBackground();

        int r = colorBackgroundDefault.getRed();
        int g = colorBackgroundDefault.getGreen();
        int b = colorBackgroundDefault.getBlue();

        int rh = (r - CHANGE < 0) ? (r + CHANGE) : (r - CHANGE);
        int gh = (g - CHANGE < 0) ? (g + CHANGE) : (g - CHANGE);
        int bh = (b - CHANGE < 0) ? (b + CHANGE) : (b - CHANGE);

        Color colorBackgroundHover = new Color(rh, gh, bh);

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setForeground(colorBackgroundHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setForeground(colorBackgroundDefault);
            }
        });
        
        
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                
                for(LinkEventAdapter e : events) {
                    e.linkClick();
                }
                
            }
        
        });

    }

    public void addLinkEvent(LinkEventAdapter evt) {
        events.add(evt);
    }

}
