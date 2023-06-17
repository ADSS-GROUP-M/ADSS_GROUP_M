package presentationLayer.gui.plAbstracts;

import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class ScrollablePanel extends Panel{

    protected final JScrollPane scrollPane;

    protected ScrollablePanel() {
        super();
        this.scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
    }

    protected ScrollablePanel(String fileName) {
        super(fileName);
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
        scrollPane.setPreferredSize(new Dimension((int)(newSize.getWidth()*0.8), (int)newSize.getHeight()));
        scrollPane.revalidate();
    }
}
