package presentationLayer.gui.employeeModule.view.panels;

import presentationLayer.gui.plAbstracts.ScrollablePanel;

import javax.swing.*;
import java.awt.*;

public class EmployeesPanel extends ScrollablePanel {

    public EmployeesPanel() {
        super();
        init();
    }

    public void init() {
        JLabel header = new JLabel("Welcome to the Employees Module!");
        header.setVerticalAlignment(JLabel.TOP);
        panel.add(header);
    }
}