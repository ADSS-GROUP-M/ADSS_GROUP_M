package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.Panel;
import presentationLayer.gui.plAbstracts.interfaces.UIElementObserver;
import presentationLayer.gui.plUtils.JPanelWithBackground;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractPanel implements Panel {

    protected final JPanel panel;
    protected final Set<UIElementObserver> observers;

    protected AbstractPanel(){
        panel = new JPanelWithBackground();
        panel.paintComponents(panel.getGraphics());
        panel.setBorder(new EmptyBorder(0,0,0,0));
        observers = new HashSet<>();
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocus();
                SwingUtilities.invokeLater(panel::repaint);
            }
        });
    }

    public void add(Component component){
        panel.add(component);
    }

    public abstract Component getComponent();
    public void componentResized(Dimension newSize) {
        panel.setPreferredSize(new Dimension((int)(newSize.width*0.8)-20,newSize.height-40));
    }

    @Override
    public void subscribe(UIElementObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(UIElementObserver observer) {
        observers.remove(observer);
    }

    @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
    @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {panel.repaint();}
    @Override public void popupMenuCanceled(PopupMenuEvent e) {}
}
