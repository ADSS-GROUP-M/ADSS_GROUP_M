package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 public class AddOrRemoveElementsList extends JPanel {

    JList list;

    DefaultListModel model;

    int counter = 15;

    public AddOrRemoveElementsList() {
        setLayout(new BorderLayout());
        model = new DefaultListModel();
        list = new JList(model);
        JScrollPane pane = new JScrollPane(list);
        JButton addButton = new JButton("Add Element");
        JButton removeButton = new JButton("Remove Element");
        for (int i = 0; i < 15; i++)
            model.addElement("Element " + i);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.addElement("Element " + counter);
                counter++;
            }
        });
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (model.getSize() > 0)
                    model.removeElementAt(0);
            }
        });

        add(pane, BorderLayout.NORTH);
        add(addButton, BorderLayout.WEST);
        add(removeButton, BorderLayout.CENTER);
    }

    public static void main(String s[]) {
        JFrame frame = new JFrame("List Model Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new AddOrRemoveElementsList());
        frame.setSize(260, 200);
        frame.setVisible(true);
    }
}
