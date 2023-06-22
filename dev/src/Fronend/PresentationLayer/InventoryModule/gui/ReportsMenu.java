package Fronend.PresentationLayer.InventoryModule.gui;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.Record;
import Backend.ServiceLayer.InventoryModule.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReportsMenu extends InventoryMainMenu {
    private String branch;

    public void run(String branch) {
        this.branch = branch;
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setSize(600, 400); // Set the size of the Reports Menu frame
        panel.setLayout(new GridLayout(6, 1));

        JLabel titleLabel = new JLabel("Please select an option:");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton getProductReportsButton = new JButton("Products Pair Category Report");
        JButton getRunningOutReportsButton = new JButton("Running-out products report");
        JButton getDefectiveReportsButton = new JButton("Defective products report");
        JButton backButton = new JButton("Back to Main Menu");

        getProductReportsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getProductPerCategory();
            }
        });

        getRunningOutReportsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inventoryRunningOutReport();
            }
        });

        getDefectiveReportsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inventoryDefectiveReport();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InventoryMainMenu.main(new String[0]);
                frame.dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(getProductReportsButton);
        panel.add(getRunningOutReportsButton);
        panel.add(getDefectiveReportsButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void getProductPerCategory() {
        JFrame inputFrame = new JFrame("Get Products Reports");
        JPanel panel = new JPanel(new GridLayout(2, 1));

        JLabel categoriesLabel = new JLabel("Enter categories' names as a list [Dairy products, Bath products, Cleaning products]:");
        JTextField categoriesTextField = new JTextField(30);

        panel.add(categoriesLabel);
        panel.add(categoriesTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Get Products Reports", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String subcategories_name = categoriesTextField.getText();
            List<String> subcategories;
            if (subcategories_name.isEmpty()) {
                subcategories = List.of(); // Creating an empty List
            } else {
                subcategories = List.of(subcategories_name.split(","));
            }
            String output = "";
            Response response = categoriesService.getCategoryReport(branch, subcategories);
            if (response.errorOccurred()) {
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                List<Record> records = (List<Record>) response.getReturnValue();
                for (Record r : records) {
                    output += r.toString() + "\n";
                }
                JOptionPane.showMessageDialog(inputFrame, output, "Product Reports", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        inputFrame.dispose();
    }

    private void inventoryRunningOutReport() {
        JFrame inputFrame = new JFrame("Inventory Running-out Report");

        String output = "";
        Response response = stockService.getShortagesProducts(Branch.valueOf(branch));
        if (response.errorOccurred()) {
            JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            List<Record> records = (List<Record>) response.getReturnValue();
            for (Record r : records) {
                output += r.toString() + "\n";
            }
            JOptionPane.showMessageDialog(inputFrame, output, "Running-out Products Report", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

    private void inventoryDefectiveReport() {
        JFrame inputFrame = new JFrame("Inventory Defective Report");

        String output = "";
        Response response = stockService.getDefectiveProducts(Branch.valueOf(branch));
        if (response.errorOccurred()) {
            JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            List<Record> records = (List<Record>) response.getReturnValue();
            for (Record r : records) {
                output += r.toString() + "\n";
            }
            JOptionPane.showMessageDialog(inputFrame, output, "Defective Products Report", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

}
