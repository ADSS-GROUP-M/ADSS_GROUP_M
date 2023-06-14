package presentationLayer.gui.plUtils;


import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Link implements UIElement, ActionListener {

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
        button.addActionListener(this);
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

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        onClick.run();
    }
}
