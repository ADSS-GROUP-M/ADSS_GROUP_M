package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.interfaces.UIElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static presentationLayer.gui.plUtils.Fonts.textBoxFont;

public class PrettyTextField implements UIElement {

    private final JTextField textField;
    private boolean clearTextOnclick;
    private final String defaultText;
    private int maxChars;

    public PrettyTextField(Dimension size, String defaultText){
        this.defaultText = defaultText;
        textField = new JTextField();
        textField.setBorder(new TextFieldBorder());
        textField.setPreferredSize(size);
        textField.setFont(textBoxFont);
        textField.setText(defaultText);
        clearTextOnclick = true;
        maxChars = -1;

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if(clearTextOnclick){
                    textField.setText("");
                    clearTextOnclick = false;
                }
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if(textField.getText().equals("")){
                    textField.setText(defaultText);
                    clearTextOnclick = true;
                }
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(maxChars != -1 && textField.getText().length() == maxChars){
                    e.consume();
                }
            }
        });
    }

    public PrettyTextField(Dimension size){
        this(size, "");
    }

    public void setHorizontalAlignment(int alignment) {
        textField.setHorizontalAlignment(alignment);
    }

    public void setMaximumCharacters(int maxChars) {
        this.maxChars = maxChars;
    }
    public String getText(){
        return textField.getText();
    }

    @Override
    public Component getComponent() {
        return textField;
    }

    @Override
    public void componentResized(Dimension newSize) {
        // do nothing
    }

    public void setText(String text){
        textField.setText(text);
    }
}
