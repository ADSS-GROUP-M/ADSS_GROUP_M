package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.interfaces.UIElement;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrettyButton extends JButton {

    public PrettyButton(String text){
        super(text);
        setBackground(Colors.getBackgroundColor());
    }

}
