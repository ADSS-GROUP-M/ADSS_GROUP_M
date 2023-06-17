package presentationLayer.gui.employeeModule.view.panels;

import presentationLayer.gui.plAbstracts.ScrollablePanel;

import javax.swing.*;
import java.awt.*;

public class EmployeesPanel extends ScrollablePanel {

    private JScrollPane scrollPane;

    public EmployeesPanel() {
        super();
        init();
    }

    public void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setSize((int)(d.getWidth()*0.8), d.height);
        JLabel header = new JLabel("Welcome to the Employees Module!");
        header.setVerticalAlignment(JLabel.TOP);
        panel.add(header);
        panel.setAutoscrolls(true);
        scrollPane = new JScrollPane(panel);
        scrollPane.setSize((int)(d.getWidth()*0.8), d.height);
    }

    @Override
    public Component getComponent() {
        return scrollPane;
    }

    @Override
    public Object getUpdate() {
        return null;
    }
}