package presentationLayer.plAbstracts;

import javax.swing.*;
import java.awt.*;

public abstract class Panel implements UIComponent {

    protected final JPanel panel;

    protected Panel(){
        panel = new JPanel();
    }

    public Component getComponent(){
        return panel;
    }
}
