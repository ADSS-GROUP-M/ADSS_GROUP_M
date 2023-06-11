package presentationLayer.plAbstracts;

import presentationLayer.plUtils.Panel1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Window {

    private final String title;
    private final JFrame frame;
    protected final ArrayList<UIComponent> components;
    protected Panel currentPanel;
    
    protected Window(String title, UIComponent ... components){
        this.title = title;
        this.components = new ArrayList<>(Arrays.asList(components));
        frame = new JFrame();
        currentPanel = new Panel1();
    }

    private void init(){
        frame.setTitle(title);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        for (UIComponent component : components) {
            component.setVisible(false);
            frame.add((JComponent) component);
        }
        frame.add(currentPanel.getComponent());
        frame.setVisible(true);
        currentPanel.setVisible(true);
    }

    public static void main(String[] args) {
        Window window = new Window("Test");
        window.init();
    }
}
