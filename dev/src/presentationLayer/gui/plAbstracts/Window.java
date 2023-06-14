package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.transportModule.panels.ViewTransportsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;

public abstract class Window extends ComponentAdapter {

    private final JFrame frame;
    private final String title;
    protected final Set<UIElement> components;
    
    protected Window(String title){
        this.title = title;
        frame = new JFrame();
        components = new HashSet<>();
        frame.addComponentListener(this);
    }

    protected void init(){
        frame.setTitle(title);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        components.forEach(component -> frame.add(component.getComponent()));
    }

    protected void addComponent(UIElement component){
        components.add(component);
    }

    protected void removeComponent(UIElement component){
        components.remove(component);
    }

    protected void setVisible(boolean b){
        frame.setVisible(b);
    }

    @Override
    public void componentResized(ComponentEvent e){
        components.forEach(component -> component.componentResized(frame.getSize()));
    }
}
