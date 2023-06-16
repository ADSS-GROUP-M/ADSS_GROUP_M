package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plUtils.examples.JPanelWithBackground;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.EventListener;

public abstract class Panel implements UIElement, PopupMenuListener {

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
        panel.setPreferredSize(new Dimension((int)(newSize.width*0.8),newSize.height));
        panel.revalidate();
    }

    @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
    @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {panel.repaint();}
    @Override public void popupMenuCanceled(PopupMenuEvent e) {}
}
