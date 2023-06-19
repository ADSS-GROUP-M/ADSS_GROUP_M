package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.interfaces.UIElement;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrettyButton implements UIElement {

    private JButton button;

    public PrettyButton(String text, Dimension size){
        button = new JButton();

        button.setPreferredSize(size);
        button.setText(text);

        button.setBackground(new Color(0,0,0,0));

        button.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(Colors.getForegroundColor());
                g.drawRoundRect(0,0,c.getWidth()-1,c.getHeight()-1,20,20);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0,0,0,0);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });


        button.setUI(new BasicButtonUI(){

            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);

                Graphics2D g2 = (Graphics2D)g;

                g2.setColor(new Color(200,200,200,64));
                g2.fillRoundRect(1,1,c.getWidth()-2,c.getHeight()-2,20,20);
            }

            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                super.paintButtonPressed(g, b);

                Graphics2D g2 = (Graphics2D)g;

                g2.setColor(new Color(200,200,200,128));
                g2.fillRoundRect(1,1,b.getWidth()-2,b.getHeight()-2,20,20);
            }
        });

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(200,200,200,64));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0,0,0,0));
            }
        });




    }

    @Override
    public Component getComponent() {
        return button;
    }

    @Override
    public void componentResized(Dimension newSize) {
        // do nothing
    }

    public static void main(String[] args){

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLayout(new GridBagLayout());

        frame.add(new PrettyButton("Hello", new Dimension(100,30)).getComponent());

        frame.setVisible(true);
    }


}
