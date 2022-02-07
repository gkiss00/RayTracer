package app.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImagePanel extends JPanel {
    private Image image;
    public ImagePanel(String path) {
        try {
            this.image = ImageIO.read(new File(path));
        } catch (Exception e) {
            System.err.println("Bad file");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 800, 400,  this);
    }
}
