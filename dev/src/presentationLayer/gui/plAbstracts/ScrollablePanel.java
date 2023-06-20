package presentationLayer.gui.plAbstracts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class ScrollablePanel extends AbstractPanel {

    protected final JScrollPane scrollPane;

    protected ScrollablePanel() {
        super();
        this.scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    @Override
    public Component getComponent() {
        return scrollPane;
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        scrollPane.setPreferredSize(panel.getPreferredSize());
//        scrollPane.revalidate();
    }
}
