/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.helper;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Lenovo
 */
public class ImageHelper {

    public ImageIcon openImage(String uri) {
        return new ImageIcon(getClass().getResource("/eco/app/icon/" + uri));
    }



    public static Image resize(Image image) {
        int w = SaveData.SIZE.width;
        int h = SaveData.SIZE.height;
        int W = image.getWidth(null);
        int H = image.getHeight(null);

        int scareW;
        int scareH;

        if (W / (H * 1.0) < 1.0) {
            scareW = (int) (((double) h / H) * W);
            scareH = h;
        } else {
            scareW = w;
            scareH = (int) (((double) w / W) * H);
        }
        return image.getScaledInstance(scareW, scareH, BufferedImage.TYPE_4BYTE_ABGR);

    }

    public static byte[] imageToByte(Image image, String type) throws IOException {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, type, baos);

        return baos.toByteArray();

    }

    public static Image createImage(byte[] data, String type) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BufferedImage image = ImageIO.read(bais);
        return image;
    }

}
