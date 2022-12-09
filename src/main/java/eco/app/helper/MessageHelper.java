/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.helper;

import eco.app.dialog.MessageDialog;
import eco.app.dialog.MessageDialog.DialogButton;
import eco.app.dialog.MessageDialog.MessageType;
import java.awt.Component;
import java.awt.Frame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lenovo
 */
public class MessageHelper {

    private static MessageDialog openDialog(Component component) {
        Frame frame = null;
        try {
            frame = (Frame) SwingUtilities.getWindowAncestor(component);
        } catch (Exception e) {
        }
        return new MessageDialog(frame);
    }

    public static MessageType showConfirm(Component component, String message) {
        MessageDialog dialog = openDialog(component);
        dialog.setText(message);
        dialog.setVisible(true);
        return dialog.getMessageType();
    }

    public static void showMessage(Component component, String message) {
        // MessageDialog dialog = openDialog(component);
        // dialog.setText(message);
        // dialog.hidenButton(DialogButton.BTN_NO, DialogButton.BTN_CANCEL);
        // dialog.setIcon(new ImageHelper().openImage("message.png"));
        // dialog.setVisible(true);
        JOptionPane.showMessageDialog(component, message);
    }

    public static void showException(Component component, Exception e) {
        MessageDialog dialog = openDialog(component);
        dialog.setText(e.getMessage());
        dialog.hidenButton(DialogButton.BTN_NO, DialogButton.BTN_CANCEL);
        dialog.setIcon(new ImageHelper().openImage("message.png"));
        dialog.setVisible(true);
        e.printStackTrace();
    }

    public static void showErrorMessage(Component component, String message) {
        MessageDialog dialog = openDialog(component);
        dialog.setText(message);
        dialog.hidenButton(DialogButton.BTN_NO, DialogButton.BTN_CANCEL);
        dialog.setIcon(new ImageHelper().openImage("message.png"));
        dialog.setVisible(true);
    }
}
