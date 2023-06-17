package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plUtils.JPanelWithBackground;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class Panel implements ObservableUIElement, PopupMenuListener {

    protected final JPanel panel;
    protected final Map<UIElementEvent, Set<UIElementObserver>> observers;

    protected Panel(){
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
            observer.update(this, event);
        }
    }

    @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
    @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {panel.repaint();}
    @Override public void popupMenuCanceled(PopupMenuEvent e) {}
}
