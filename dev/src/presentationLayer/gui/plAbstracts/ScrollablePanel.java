package presentationLayer.gui.plAbstracts;

import javax.swing.*;
import java.awt.*;

public abstract class ScrollablePanel extends Panel{

    protected final JScrollPane scrollPane;

    protected ScrollablePanel() {
        super();
        this.scrollPane = new JScrollPane(panel);
    }

    protected ScrollablePanel(String fileName) {
        super(fileName);
        this.scrollPane = new JScrollPane(panel);
    }

    @Override
    public Component getComponent() {
        return scrollPane;
    }

    @Override
    public void componentResized(Dimension newSize) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        scrollPane.setBounds((int)(d.width*0.2), 0, (int)(newSize.getWidth()*0.8), (int)newSize.getHeight());
        scrollPane.revalidate();
    }
}
