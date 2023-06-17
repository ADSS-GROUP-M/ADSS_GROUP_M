package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ContentPanel extends JPanel {

    private static final int MAX_ALPHA = 255;
    private static final int MIN_ALPHA = 230;
    public static final Color COLOR = new Color(255, 255, 255, MIN_ALPHA);

    public ContentPanel(Color backgroundColor){
        super();
        setBackground(backgroundColor);
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(FavoriteColors.coolOrangeTransparent);
                //bottom
                g.fillRect(0,height-10,width,height);

                //top-left corner
                g.fillRect(0,0,width/5,10);
                g.fillRect(0,10,10,height/5-10);

                //top-right corner
                g.fillRect(width-10,10,width,height/5-10);
                g.fillRect(width-width/5,0,width,10);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0,0,5,0);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });

//        addMouseListener(new MouseAdapter() {
//
//            int i = MIN_ALPHA;
//            Timer timer;
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//
//                if(timer != null && timer.isRunning()){
//                    timer.stop();
//                }
//                timer = new Timer(10, e2 -> {
//                    if (i >= MAX_ALPHA) {
//                        timer.stop();
//                    } else {
//                        i++;
//                        setBackground(new Color(COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), i));
//                        getParent().repaint();
//                    }
//                });
//                timer.start();
//            }
//            @Override
//            public void mouseExited(MouseEvent e) {
//
//                if(timer != null && timer.isRunning()){
//                    timer.stop();
//                }
//                timer = new Timer(10, e2 -> {
//                    if (i <= MIN_ALPHA) {
//                        timer.stop();
//                    } else {
//                        i--;
//                        setBackground(new Color(COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), i));
//                        getParent().repaint();
//                    }
//                });
//                timer.start();
//            }
//        });
    }
}
