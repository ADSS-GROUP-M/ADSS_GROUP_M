package presentationLayer.gui.plAbstracts;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public abstract class Window {

    private final JFrame frame;
    private final String title;
    protected final List<UIElement> components;
    protected Panel currentPanel;
    
    protected Window(String title, Panel startingPanel){
        this.title = title;
        frame = new JFrame();
        components = new LinkedList<>();
        currentPanel = startingPanel;
    }

    protected void setCurrentPanel(Panel p) {
        currentPanel = p;
        frame.remove(currentPanel.getComponent());
        frame.add(currentPanel.getComponent());
        frame.repaint();
    }

    protected void init(){
        frame.setTitle(title);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.add(currentPanel.getComponent());
        components.forEach(component -> frame.add(component.getComponent()));
    }

    protected void addComponent(UIElement component){
        components.add(component);
    }
    protected void setVisible(boolean b){
        frame.setVisible(b);
    }
}
