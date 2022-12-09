/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.myswing;

import eco.app.helper.SaveData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Lenovo
 */
public class TableCustom extends JTable {

    public TableCustom() {
        setShowVerticalLines(true);
        setShowVerticalLines(false);
        setGridColor(new Color(153, 153, 153));
        setRowHeight(30);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                TableHeader header = new TableHeader(o + "");
                if (i1 == 4) {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });

        setMinimumSize(new Dimension(100, 100));

//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
//        setDefaultRenderer(String.class, centerRenderer);
//
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        setDefaultRenderer(Integer.class, leftRenderer);
        setDefaultRenderer(Long.class, leftRenderer);
        setDefaultRenderer(Double.class, leftRenderer);

        setSelectionBackground(SaveData.TBL_SELECTED_BG_COLOR);
        setSelectionForeground(SaveData.TBL_SELECTED_FG_COLOR);

    }

    public void fixTable(JScrollPane scroll) {

        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBar(new ScrollBarCustom());
        setGridColor(SaveData.GRIL_TABLE_COLOR);

        // 
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        scroll.setBorder(new EmptyBorder(5, 10, 5, 10));
    }

}
