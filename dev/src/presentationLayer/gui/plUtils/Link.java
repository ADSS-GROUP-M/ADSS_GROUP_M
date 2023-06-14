package presentationLayer.gui.plUtils;


import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Objects;

public class Link implements UIElement {

    private final String name;
    private final Runnable onClick;
    private JLabel label;

    /**
     * @throws NullPointerException if name or onClick is null
     */
    public Link (String name, Runnable onClick) {
        this.name = Objects.requireNonNull(name);
        this.onClick = Objects.requireNonNull(onClick);
        label = new JLabel(name);
    }

    public void click(){
        onClick.run();
    }

    public String getName() {
        return name;
    }

    @Override
    public Component getComponent() {
        return label;
    }

    @Override
    public void componentResized(Dimension newSize) {

    }
}
