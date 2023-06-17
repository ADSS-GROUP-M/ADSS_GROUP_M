package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;

import static presentationLayer.gui.plUtils.Fonts.textBoxFont;

public class PrettyTextField implements UIElement {

    private final JTextField textField;

    public PrettyTextField(Dimension size){
        textField = new JTextField();
        textField.setBorder(new TextFieldBorder());
        System.out.println(textField.getPreferredSize());
        textField.setPreferredSize(size);
        textField.setFont(textBoxFont);
    }

    @Override
    public Component getComponent() {
        return textField;
    }

    @Override
    public void componentResized(Dimension newSize) {
        // do nothing
    }

}
