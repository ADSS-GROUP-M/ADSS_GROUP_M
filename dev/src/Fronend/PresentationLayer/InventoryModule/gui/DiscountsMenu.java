package Fronend.PresentationLayer.InventoryModule.gui;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.ServiceLayer.InventoryModule.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiscountsMenu extends InventoryMainMenu {

    private String branch;

    public void run(String branch) {
        this.branch = branch;
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setSize(600, 400); // Set the size of the Discounts Menu frame
        panel.setLayout(new GridLayout(6, 1));

        JLabel titleLabel = new JLabel("Please select an option:");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton addProductDiscountButton = new JButton("Add product's discount");
        JButton addCategoryDiscountButton = new JButton("Add category's discount");
        JButton backButton = new JButton("Back to Main Menu");

        addProductDiscountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProductDiscount();
            }
        });

        addCategoryDiscountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategoryDiscount();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InventoryMainMenu.main(new String[0]);
                frame.dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(addProductDiscountButton);
        panel.add(addCategoryDiscountButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addProductDiscount() {
        JFrame inputFrame = new JFrame("Add Product's Discount");
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel catalogNumberLabel = new JLabel("Product Catalog Number:");
        JTextField catalogNumberTextField = new JTextField(10);
        JLabel discountLabel = new JLabel("Discount (%):");
        JTextField discountTextField = new JTextField(10);
        JLabel startDateLabel = new JLabel("Start Date (dd-MM-yyyy HH:mm:ss):");
        JTextField startDateTextField = new JTextField(19);
        JLabel endDateLabel = new JLabel("End Date (dd-MM-yyyy HH:mm:ss):");
        JTextField endDateTextField = new JTextField(19);

        panel.add(catalogNumberLabel);
        panel.add(catalogNumberTextField);
        panel.add(discountLabel);
        panel.add(discountTextField);
        panel.add(startDateLabel);
        panel.add(startDateTextField);
        panel.add(endDateLabel);
        panel.add(endDateTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Add Product's Discount", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String catalogNumber = catalogNumberTextField.getText();
            double discount = Double.parseDouble(discountTextField.getText());
            String startDate = startDateTextField.getText();
            String endDate = endDateTextField.getText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime parsedStartDate = LocalDateTime.parse(startDate, formatter);
            LocalDateTime parsedEndDate = LocalDateTime.parse(endDate, formatter);

            // Call the method to add a discount to the product with the obtained values
            Response response = stockService.updateDiscountPerProduct(catalogNumber, Branch.valueOf(branch), discount, parsedStartDate, parsedEndDate);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

    private void addCategoryDiscount() {
        JFrame inputFrame = new JFrame("Add Category's Discount");
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel categoryNameLabel = new JLabel("Category Name:");
        JTextField categoryNameTextField = new JTextField(10);
        JLabel discountLabel = new JLabel("Discount (%):");
        JTextField discountTextField = new JTextField(10);
        JLabel startDateLabel = new JLabel("Start Date (dd-MM-yyyy HH:mm:ss):");
        JTextField startDateTextField = new JTextField(19);
        JLabel endDateLabel = new JLabel("End Date (dd-MM-yyyy HH:mm:ss):");
        JTextField endDateTextField = new JTextField(19);

        panel.add(categoryNameLabel);
        panel.add(categoryNameTextField);
        panel.add(discountLabel);
        panel.add(discountTextField);
        panel.add(startDateLabel);
        panel.add(startDateTextField);
        panel.add(endDateLabel);
        panel.add(endDateTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Add Category's Discount", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameTextField.getText();
            double discount = Double.parseDouble(discountTextField.getText());
            String startDate = startDateTextField.getText();
            String endDate = endDateTextField.getText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime parsedStartDate = LocalDateTime.parse(startDate, formatter);
            LocalDateTime parsedEndDate = LocalDateTime.parse(endDate, formatter);

            // Call the method to add a discount to the category with the obtained values
            Response response = stockService.updateDiscountPerCategory(categoryName, Branch.valueOf(branch), discount, parsedStartDate, parsedEndDate);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }
}
