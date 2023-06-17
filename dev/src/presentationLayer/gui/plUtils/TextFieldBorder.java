package presentationLayer.gui.plUtils;

import javax.swing.border.Border;
import java.awt.*;

class TextFieldBorder implements Border {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(FavoriteColors.coolOrange);
        g.fillRect(x, height - 2, width, 2); // bottom
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, width, 1); // top
        g.fillRect(width - 1, 0, 1, height); // right
        g.fillRect(0, 0, 1, height); // left
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(1, 1, 2, 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
