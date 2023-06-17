package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.UIElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashSet;
import java.util.Set;

public abstract class MainWindow {

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    protected final JFrame container;
    protected final String title;
    private final Set<UIElement> components;
    
    protected MainWindow(String title){
        this.title = title;
        container = new JFrame();
        components = new HashSet<>();
        container.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                components.forEach(component -> component.componentResized(container.getSize()));
                container.revalidate();
            }
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                componentResized(e);
            }
        });
    }

    protected void init(){
        container.setTitle(title);
        container.setBackground(Color.WHITE);
        container.setMinimumSize(new Dimension(1024, 768));
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
}
