package presentationLayer.gui.plAbstracts;

import javax.swing.*;
import java.awt.*;

public class PanelHolder {

    private CardLayout cardLayout;
    private JPanel panel;

    public PanelHolder() {
        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);
    }

    public void setPanel(Panel panel, String name) {
        this.panel.add(name,panel.getComponent());
        cardLayout.show(this.panel, name);
    }

}
