package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plUtils.JPanelWithBackground;

import javax.swing.*;
import java.awt.*;

public abstract class Panel implements UIElement {

    protected final JPanel panel;
    protected Panel(){
        panel = new JPanel();
    }
    protected Panel(String fileName){
        panel = new JPanelWithBackground(fileName);
        panel.paintComponents(panel.getGraphics());
    }

    public void add(Component component){
        panel.add(component);
    }

    public abstract Component getComponent();
    public void componentResized(Dimension newSize) {
        panel.setSize(new Dimension((int)(newSize.width*0.8),newSize.height));
    }
}
