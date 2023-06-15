package presentationLayer.gui.plAbstracts;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

public abstract class ScrollablePanel extends Panel{

    protected final JScrollPane scrollPane;

    protected ScrollablePanel() {
        super();
        this.scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    protected ScrollablePanel(String fileName) {
        super(fileName);
        this.scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    @Override
    public Component getComponent() {
        return scrollPane;
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension d = MainWindow.screenSize;
        scrollPane.setBounds((int)(d.width*0.2), 0, (int)(newSize.getWidth()*0.8), (int)newSize.getHeight());
        scrollPane.revalidate();
    }
}
