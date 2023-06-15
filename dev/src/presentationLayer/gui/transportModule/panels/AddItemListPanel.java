package presentationLayer.gui.transportModule.panels;

import presentationLayer.gui.plAbstracts.Panel;
import presentationLayer.gui.plAbstracts.ScrollablePanel;

import javax.swing.*;
import java.awt.*;

public class AddItemListPanel extends ScrollablePanel {

    public AddItemListPanel() {
        super("src/resources/truck_main_page.jpg");
        init();
    }

    private void init() {

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setSize(scrollPane.getSize());
        JLabel header = new JLabel("Enter Item List details:\n");
        header.setVerticalAlignment(JLabel.TOP);
        panel.add(header);


        //Panel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel loadingLabel = new JLabel("Item loading list:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(loadingLabel, constraints);

        JTextField idField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(idField, constraints);






    }

}
