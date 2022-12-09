/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.model;

import eco.app.helper.ImageHelper;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;

/**
 *
 * @author Lenovo
 */
public class MenuModel {

    private String text;
    private Color colorEffect;
    private ImageIcon icon;
    private Font font;
    private Color foreground;

//    private void initDefault() {
//        text = "Menu item 01";
//        icon = new ImageIcon(getClass().getResource("/eco/app/icon/menu.png"));
//        font = new Font("Tahoma", 1, 14);
//        foreground = new Color(255, 255, 236);
//    }
    public MenuModel() {
    }

    public MenuModel(String text, String icon) {
        this.text = text;
        try {
            this.icon = new ImageHelper().openImage(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MenuModel(String text, ImageIcon icon) {
        this.text = text;
        this.icon = icon;
    }

    public MenuModel(String text, ImageIcon icon, Font font) {
        this.text = text;
        this.icon = icon;
        this.font = font;
    }

    public MenuModel(String text, Color colorEffect, ImageIcon icon, Font font, Color foreground) {
        this.text = text;
        this.colorEffect = colorEffect;
        this.icon = icon;
        this.font = font;
        this.foreground = foreground;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColorEffect() {
        return colorEffect;
    }

    public void setColorEffect(Color colorEffect) {
        this.colorEffect = colorEffect;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

}
