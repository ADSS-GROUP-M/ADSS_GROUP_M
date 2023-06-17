package presentationLayer.gui.plUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JPanelWithBackground extends JPanel {

    private final Image backgroundImage;

    public JPanelWithBackground(String fileName) {
        backgroundImage = new ImageIcon(fileName).getImage();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0,getWidth(),getHeight(), this);
    }
}
