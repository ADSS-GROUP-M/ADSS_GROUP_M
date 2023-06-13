package presentationLayer.plAbstracts;

import presentationLayer.plUtils.JPanelWithBackground;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class Panel implements UIComponent {

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
}
