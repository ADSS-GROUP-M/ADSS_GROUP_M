package presentationLayer.gui.plUtils;


import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Link implements UIElement, ActionListener {

    private final String name;
    private final Runnable onClick;
    private final JButton button;
    private final Dimension preferredSize;

    /**
     * @throws NullPointerException if name or onClick is null
     */
    public Link (String name, Runnable onClick) {
        this.name = Objects.requireNonNull(name);
        this.onClick = Objects.requireNonNull(onClick);
        button = new JButton(name);
        preferredSize = new Dimension(150, 20);
        button.setPreferredSize(preferredSize);
        button.addActionListener(this);
//        button.setBorder(new EmptyBorder(5, 15, 5, 5));
        button.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                g2.setColor(Colors.getForegroundColor());
                g2.drawLine(15,1, c.getWidth(), 1);
                g2.drawLine(15,height-1, c.getWidth(), height-1);
                g2.drawArc(2,1,c.getHeight()-2,c.getHeight()-2,90,180);
                g2.fillRect(c.getHeight()/2,0,c.getHeight()/2 , c.getHeight());
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
        button.setBackground(Colors.getBackgroundColor());
        button.setUI(new BasicButtonUI() {
            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                g2.setColor(Colors.getForegroundColor());

                g2.setColor(new Color(224, 216, 216));
                g2.fillRect(15,1, b.getWidth(), b.getHeight());
                g2.fillArc(2,1,b.getHeight()-2,b.getHeight()-2,90,180);
                g2.setColor(Colors.getForegroundColor());
                g2.fillRect(b.getHeight()/2,0,b.getHeight()/2 , b.getHeight());
                super.paintButtonPressed(g, b);
            }
        });
        button.addMouseListener(new MouseAdapter() {
            Timer timer;
            int i = 0;
            public void mouseEntered(MouseEvent evt) {
                if(timer != null && timer.isRunning()) {
                    timer.stop();
                }

                timer = new Timer(1, e -> {
                    button.setPreferredSize(new Dimension(preferredSize.width + i, preferredSize.height));
                    button.revalidate();
                    button.repaint();
                    if (i >= 10) {
                        timer.stop();
                    } else {
                        i +=2;
                    }
                });
                timer.start();
            }
            public void mouseExited(MouseEvent evt) {
                if(timer!= null && timer.isRunning()) {
                    timer.stop();
                }

                timer = new Timer(1, e -> {
                    button.setPreferredSize(new Dimension(preferredSize.width + i, preferredSize.height));
                    button.revalidate();
                    button.repaint();
                    if (i <= 0) {
                        timer.stop();
                    } else {
                        i -=2;
                    }
                });
                timer.start();
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
