package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.Searchable;
import presentationLayer.gui.plAbstracts.UIElement;

import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;


public class SearchBox implements UIElement, KeyListener, FocusListener, ComponentListener {

    private final JTextField textField;
    private final JWindow popup;
    private final List<Searchable> data;

    public SearchBox(List<Searchable> data){
        this.data = data;
        popup = new JWindow();
        textField = new JTextField();
        init();
    }

    private void init(){
        textField.addKeyListener(this);
        textField.addFocusListener(this);
        textField.setPreferredSize(new Dimension(200, 30));
        popup.setLocationRelativeTo(textField);
        popup.setLocation(popup.getX(), popup.getY()+30);
        popup.setBackground(Color.lightGray);
        popup.setSize(200, 100);
    }

    public void setComponentListener(Container container){
        container.addComponentListener(this);
    }


    @Override
    public Component getComponent() {
        return textField;
    }

    @Override
    public void componentResized(Dimension newSize) {

    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Invoked when a component gains the keyboard focus.
     *
     * @param e the event to be processed
     */
    @Override
    public void focusGained(FocusEvent e) {
        Point location = textField.getLocationOnScreen();
        popup.setLocation(location.x, location.y+30);
        popup.setVisible(true);
        System.out.println("focus gained");
    }

    /**
     * Invoked when a component loses the keyboard focus.
     *
     * @param e the event to be processed
     */
    @Override
    public void focusLost(FocusEvent e) {
        popup.setVisible(false);
        System.out.println("focus lost");
    }

    public static void main(String[] args){


        //generate some data

        Searchable s1 = new Searchable() {
            final String description = "s1";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getDescription() {
                return description;
            }
        };

        Searchable s2 = new Searchable() {
            final String description = "s2";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getDescription() {
                return description;
            }
        };

        Searchable s3 = new Searchable() {
            final String description = "s3";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getDescription() {
                return description;
            }
        };

        Searchable s4 = new Searchable() {
            final String description = "s4";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getDescription() {
                return description;
            }
        };

        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.add(new SearchBox(List.of(s1,s2,s3,s4)).getComponent());
        frame.add(new JButton("test"));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Invoked when the component's size changes.
     *
     * @param e the event to be processed
     */
    @Override
    public void componentResized(ComponentEvent e) {

    }

    /**
     * Invoked when the component's position changes.
     *
     * @param e the event to be processed
     */
    @Override
    public void componentMoved(ComponentEvent e) {
        System.out.println("component moved");
        popup.setVisible(false);
    }

    /**
     * Invoked when the component has been made visible.
     *
     * @param e the event to be processed
     */
    @Override
    public void componentShown(ComponentEvent e) {

    }

    /**
     * Invoked when the component has been made invisible.
     *
     * @param e the event to be processed
     */
    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
