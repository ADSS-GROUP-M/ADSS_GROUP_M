package presentationLayer.gui.plUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContentPanel extends JPanel {

    public ContentPanel(Color backgroundColor){
        super();
        setBackground(backgroundColor);
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(Colors.getTrasparentForegroundColor());
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                requestFocus();
            }
        });
    }
}
