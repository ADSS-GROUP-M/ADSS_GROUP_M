package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.Panel;
import presentationLayer.gui.plAbstracts.interfaces.UIElementObserver;
import presentationLayer.gui.plUtils.JPanelWithBackground;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import java.awt.*;
import java.util.*;

public abstract class AbstractPanel implements Panel {

    protected final JPanel panel;
    protected final Map<UIElementEvent, Set<UIElementObserver>> observers;

    protected AbstractPanel(){
        panel = new JPanelWithBackground();
        panel.paintComponents(panel.getGraphics());
        panel.setBorder(new EmptyBorder(0,0,0,0));
        observers = new HashMap<>();
    }

    public void add(Component component){
        panel.add(component);
    }

    public abstract Component getComponent();
    public void componentResized(Dimension newSize) {
        panel.setPreferredSize(new Dimension((int)(newSize.width*0.8)-20,newSize.height-40));
        panel.revalidate();
    }

    @Override
    public void subscribe(UIElementObserver observer, UIElementEvent event) {
        if(!observers.containsKey(event)) {
            observers.put(event, new HashSet<>());
        }
        observers.get(event).add(observer);
    }

    @Override
    public void unsubscribe(UIElementObserver observer, UIElementEvent event) {
        observers.get(event).remove(observer);
    }

    @Override
    public void notifyObservers(UIElementEvent event) {
        for(UIElementObserver observer : observers.get(event)) {
            observer.notify(this, event);
        }
    }

    @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
    @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {panel.repaint();}
    @Override public void popupMenuCanceled(PopupMenuEvent e) {}
}
