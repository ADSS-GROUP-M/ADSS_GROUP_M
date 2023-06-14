package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.transportModule.panels.ViewTransportsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;

public abstract class Window extends ComponentAdapter {

    private final JFrame frame;
    private final JPanel panel;
    protected final String title;
    protected final Set<UIElement> components;
    
    protected Window(String title){
        this.title = title;
        frame = new JFrame();
        panel = new JPanel();
        frame.add(panel);
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
    }

    protected void addComponent(UIElement component){
        components.add(component);
        panel.add(component.getComponent());
        panel.revalidate();
        panel.repaint();
    }

    protected void removeComponent(UIElement component){
        components.remove(component);
        panel.remove(component.getComponent());
        panel.revalidate();
        panel.repaint();
    }

    protected void setVisible(boolean b){
        frame.setVisible(b);
    }

    @Override
    public void componentResized(ComponentEvent e){
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        components.forEach(component -> component.componentResized(panel.getSize()));
    }
}
