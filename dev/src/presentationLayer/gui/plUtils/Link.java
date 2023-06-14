package presentationLayer.gui.plUtils;


import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Link implements UIElement {

    private final String name;
    private final Runnable onClick;
    private final JButton button;

    /**
     * @throws NullPointerException if name or onClick is null
     */
    public Link (String name, Runnable onClick) {
        this.name = Objects.requireNonNull(name);
        this.onClick = Objects.requireNonNull(onClick);
        button = new JButton(name);
        button.setPreferredSize(new Dimension(150, 20));
    }

    public void click(){
        onClick.run();
    }

    public String getName() {
        return name;
    }

    @Override
    public Component getComponent() {
        return button;
    }

    @Override
    public void componentResized(Dimension newSize) {

    }
}
