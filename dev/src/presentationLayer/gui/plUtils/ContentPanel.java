package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {

    public static final Color COLOR = new Color(255, 255, 255, 200);

    public ContentPanel(Color backgroundColor){
        super();
        setBackground(backgroundColor);
    }
}
