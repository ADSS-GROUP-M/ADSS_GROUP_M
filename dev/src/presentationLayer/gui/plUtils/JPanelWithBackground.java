package presentationLayer.gui.plUtils;

import javax.swing.*;
import java.awt.*;

public class JPanelWithBackground extends JPanel {

    private final Image backgroundImage;

    public JPanelWithBackground() {
        backgroundImage = Colors.getBackgroundImage();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0,getWidth(),getHeight(), this);
    }
}
