package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plUtils.examples.JPanelWithBackground;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

public abstract class Panel implements UIElement {

    protected final JPanel panel;
    protected final PopupMenuListener popupMenuListener;

    protected Panel(){
        panel = new JPanel();
        popupMenuListener = getPopupMenuListener();
    }
    protected Panel(String fileName){
        panel = new JPanelWithBackground(fileName);
        panel.paintComponents(panel.getGraphics());
        popupMenuListener = getPopupMenuListener();
    }

    public void add(Component component){
        panel.add(component);
    }

    public abstract Component getComponent();
    public void componentResized(Dimension newSize) {
        panel.setPreferredSize(new Dimension((int)(newSize.width*0.8),newSize.height));
        panel.revalidate();
    }

    private PopupMenuListener getPopupMenuListener(){
        return new PopupMenuListener() {
            @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
            @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {panel.repaint();}
            @Override public void popupMenuCanceled(PopupMenuEvent e) {}
        };
    }
}
