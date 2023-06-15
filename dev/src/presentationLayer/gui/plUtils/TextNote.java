package presentationLayer.gui.plUtils;

import javax.swing.*;

public class TextNote extends JTextArea {
    public TextNote(String text) {
        super(text);
        setBackground(null);
        setOpaque(false);
        setEditable(false);
        setBorder(null);
        setLineWrap(true);
        setWrapStyleWord(true);
        setFocusable(false);
    }
}
