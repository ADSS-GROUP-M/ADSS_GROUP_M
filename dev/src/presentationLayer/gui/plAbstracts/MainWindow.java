package presentationLayer.gui.plAbstracts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;

public abstract class MainWindow extends ComponentAdapter {

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    protected final JFrame container;
    protected final String title;
    private final Set<UIElement> components;
    
    protected MainWindow(String title){
        this.title = title;
        container = new JFrame();
        components = new HashSet<>();
        container.addComponentListener(this);
    }

    protected void init(){
        container.setTitle(title);
        container.setBackground(Color.WHITE);
        container.setMinimumSize(new Dimension(800, 600));
        container.setExtendedState(JFrame.MAXIMIZED_BOTH);
        container.setLocationRelativeTo(null);
        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.setLayout(null);
        components.forEach(component -> container.add(component.getComponent()));
        container.pack();
    }

    protected void addUIElement(UIElement component) {
        components.add(component);
    }

    protected void setVisible(boolean b){
        container.setVisible(b);
    }

    @Override
    public void componentResized(ComponentEvent e){
        components.forEach(component -> component.componentResized(container.getSize()));
    }

    @Override
    public void componentMoved(ComponentEvent e){
        componentResized(e);
    }
}
