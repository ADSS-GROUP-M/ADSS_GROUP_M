package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.interfaces.UIElement;

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
        Dimension d = getPreferredSize();
        g.drawImage(backgroundImage, 0, 0,d.width,d.height, this);
    }
}
