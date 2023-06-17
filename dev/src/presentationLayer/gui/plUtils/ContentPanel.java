package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ContentPanel extends JPanel {

    public static final Color COLOR = new Color(255, 255, 255, 230);

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
                return true;
            }
        });
    }
}
