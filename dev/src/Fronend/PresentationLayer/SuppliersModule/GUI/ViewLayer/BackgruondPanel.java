package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import javax.swing.*;
import java.awt.*;

public class BackgruondPanel extends JPanel {
    private Image image;
    public BackgruondPanel(String path){
        image = new ImageIcon(path).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).drawImage(image, 0,0, getWidth(), getHeight(), this);
    }
}
