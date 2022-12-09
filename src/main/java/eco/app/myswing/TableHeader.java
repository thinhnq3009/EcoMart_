/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.myswing;

import eco.app.helper.SaveData;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Lenovo
 */
public class TableHeader extends JLabel {

    public TableHeader(String text) {
        super(text);
        setOpaque(true);
        setFont(new Font("Arial", 0, 14));
        setBackground(SaveData.TBL_HEADER);
        setForeground(SaveData.TBL_TEXT_COLOR_HEADER);
        setBorder(new EmptyBorder(10, 5, 10, 5));
        setHorizontalAlignment(LEFT);
        
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(TableHeader.this.getText());
            }
            
        });
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        g2.setColor(SaveData.BTN_SUCCESS);
//        g2.drawLine(0, 0, 1, getHeight());
//        g2.drawLine(getWidth() - 1, 0, getWidth(), getHeight());
//
//        g2.dispose();
//
//    }

}
