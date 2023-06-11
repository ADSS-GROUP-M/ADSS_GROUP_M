package presentationLayer.plUtils;

import presentationLayer.plAbstracts.Panel;

import java.awt.*;

public class Panel1 extends Panel {

    public Panel1() {
        super();
        setVisible(true);
    }

    @Override
    public void setVisible(boolean val) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setSize((int)(d.getWidth()*0.8), d.height);
        panel.setBackground(new java.awt.Color(16, 65, 103, 216));

//        panel.setLocation(0,d.height / 2);
//        panel.setVisible(val);
    }

    public static void main(String[] args) {
        Panel1 panel1 = new Panel1();
        panel1.setVisible(true);
    }
}
