package presentationLayer.gui.plUtils;


import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import static presentationLayer.gui.plUtils.FavoriteColors.buttonColor;
import static presentationLayer.gui.plUtils.FavoriteColors.coolOrange;

public class Link implements UIElement, ActionListener {

    private final String name;
    private final Runnable onClick;
    private final JButton button;

    /**
     * @throws NullPointerException if name or onClick is null
     */
    public Link (String name, Runnable onClick) {
        this.name = Objects.requireNonNull(name);
        this.onClick = Objects.requireNonNull(onClick);
        button = new JButton(name);
        button.setPreferredSize(new Dimension(150, 20));
        button.addActionListener(this);
        button.setBorder(new EmptyBorder(5, 15, 5, 5));
        button.setBackground(FavoriteColors.coolOrangeBackground);
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(buttonColor);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                g2.fillRect(15,0, c.getWidth(), c.getHeight()+5);
                g2.fillArc(0,0,c.getHeight(),c.getHeight(),90,180);
                g2.setColor(coolOrange);
                g2.fillRect(c.getHeight()/2,0,c.getHeight()/2 , c.getHeight());
                super.paint(g2, c);
            }

            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(new Color(224, 216, 216));
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                g2.fillRect(15,0, b.getWidth(), b.getHeight()+5);
                g2.fillArc(0,0,b.getHeight(),b.getHeight(),90,180);
                g2.setColor(coolOrange);
                g2.fillRect(b.getHeight()/2,0,b.getHeight()/2 , b.getHeight());
                super.paintButtonPressed(g, b);
            }
        });
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setPreferredSize(new Dimension(button.getPreferredSize().width+10, button.getPreferredSize().height));
                button.revalidate();
            }

            public void mouseExited(MouseEvent evt) {
                button.setPreferredSize(new Dimension(button.getPreferredSize().width-10, button.getPreferredSize().height));
                button.revalidate();
            }
        });
    }

    public String getName() {
        return name;
    }

    @Override
    public Component getComponent() {
        return button;
    }

    @Override
    public void componentResized(Dimension newSize) {

    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        onClick.run();
    }
}
