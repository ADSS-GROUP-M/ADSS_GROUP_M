package Fronend.PresentationLayer.InventoryModule.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Backend.ServiceLayer.InventoryModule.Response;

public class ProductsMenu extends InventoryMainMenu{
    private String branch;
    private JFrame frame;

    public void run(String branch) {
        this.branch = branch;
        frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setSize(600, 400); // Set the size of the Products Menu frame
        panel.setLayout(new GridLayout(7, 1));

        JLabel titleLabel = new JLabel("Please select an option:");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton createProductButton = new JButton("Create New Product");
        JButton createItemButton = new JButton("Create New Item");
        JButton updateProductButton = new JButton("Update Product");
        JButton updateItemButton = new JButton("Update Item");
        JButton getProductDetailsButton = new JButton("Get All Product's Details");
        JButton backButton = new JButton("Back to Main Menu");

        createProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewProduct();
            }
        });

        createItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewItem();
            }
        });

        updateProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        updateItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateItem();
            }
        });

        getProductDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getProductDetails();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InventoryMainMenu.main(new String[0]);
                frame.dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(createProductButton);
        panel.add(createItemButton);
        panel.add(updateProductButton);
        panel.add(updateItemButton);
        panel.add(getProductDetailsButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createNewProduct() {
        JFrame inputFrame = new JFrame("Create New Product");
        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel catalogLabel = new JLabel("Catalog Number:");
        JTextField catalogTextField = new JTextField(10);
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField(10);
        JLabel manufacturerLabel = new JLabel("Manufacturer:");
        JTextField manufacturerTextField = new JTextField(10);
        JLabel storePriceLabel = new JLabel("Store Price:");
        JTextField storePriceTextField = new JTextField(10);

        panel.add(catalogLabel);
        panel.add(catalogTextField);
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(manufacturerLabel);
        panel.add(manufacturerTextField);
        panel.add(storePriceLabel);
        panel.add(storePriceTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Create New Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String catalogNum = catalogTextField.getText();
            String name = nameTextField.getText();
            String manufacturer = manufacturerTextField.getText();
            double storePrice = Double.parseDouble(storePriceTextField.getText());

            // Call the method to create a new product with the obtained values
            Response response = stockService.addProduct(catalogNum, name, manufacturer, storePrice, branch);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

    private void createNewItem () {
        JFrame inputFrame = new JFrame("Create New Item");
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel catalogLabel = new JLabel("Catalog Number:");
        JTextField catalogTextField = new JTextField(10);

        JLabel serialLabel = new JLabel("Serial Number (comma-separated):");
        JTextField serialTextField = new JTextField(10);

        JLabel supplierLabel = new JLabel("Supplier:");
        JTextField supplierTextField = new JTextField(10);

        JLabel priceLabel = new JLabel("Supplier Price:");
        JTextField priceTextField = new JTextField(10);

        JLabel discountLabel = new JLabel("Supplier Price after Discount:");
        JTextField discountTextField = new JTextField(10);

        JLabel locationLabel = new JLabel("Location:");
        JTextField locationTextField = new JTextField(10);

        JLabel dateLabel = new JLabel("Expiration Date (format: yyyy-MM-dd):");
        JTextField dateTextField = new JTextField(10);

        JLabel periodicLabel = new JLabel("Is it a periodic supplier? (y/n):");

        JTextField periodicTextField = new JTextField(10);
        panel.add(catalogLabel);
        panel.add(catalogTextField);
        panel.add(serialLabel);
        panel.add(serialTextField);
        panel.add(supplierLabel);
        panel.add(supplierTextField);
        panel.add(priceLabel);
        panel.add(priceTextField);
        panel.add(discountLabel);
        panel.add(discountTextField);
        panel.add(locationLabel);
        panel.add(locationTextField);
        panel.add(dateLabel);
        panel.add(dateTextField);
        panel.add(periodicLabel);
        panel.add(periodicTextField);

        inputFrame.add(panel);
        inputFrame.pack();
        inputFrame.setLocationRelativeTo(null);
        inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputFrame.setVisible(true);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Create New Product Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String catalog_num = catalogTextField.getText();
            String serial_num = serialTextField.getText();
            String[] serialNumbersArray = serial_num.split(",");
            List<String> serialNumbers = new ArrayList<>();
            for (String serialNumber : serialNumbersArray) {
                serialNumbers.add(serialNumber.trim());
            }
            String supplier = supplierTextField.getText();
            double supplier_price = Double.parseDouble(priceTextField.getText());
            double supplier_discount_price = Double.parseDouble(discountTextField.getText());
            String location = locationTextField.getText();
            String dateString = dateTextField.getText();
            LocalDate date = LocalDate.parse(dateString);
            LocalDateTime expirationDate = date.atStartOfDay();
            String periodicSupplier = periodicTextField.getText();
            Response response = stockService.addProductItem(serialNumbers, catalog_num, supplier, supplier_price, supplier_discount_price, branch, location, expirationDate, periodicSupplier);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        inputFrame.dispose();
        }

    private void updateProduct() {
        JFrame inputFrame = new JFrame("Update Product");
        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel catalogLabel = new JLabel("Catalog Number:");
        JTextField catalogTextField = new JTextField(10);

        JLabel optionLabel = new JLabel("Choose details to update:");
        JComboBox<String> optionComboBox = new JComboBox<>(new String[]{"name", "manufacturer", "store price"});

        JLabel newValueLabel = new JLabel("New Value:");
        JTextField newValueTextField = new JTextField(10);

        panel.add(catalogLabel);
        panel.add(catalogTextField);
        panel.add(optionLabel);
        panel.add(optionComboBox);
        panel.add(newValueLabel);
        panel.add(newValueTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Update Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String catalogNum = catalogTextField.getText();
            String selectedOption = (String) optionComboBox.getSelectedItem();
            String newValue = newValueTextField.getText();

            switch (selectedOption) {
                case "name" -> stockService.updateProduct(newValue, catalogNum, null, -1, -1, branch);
                case "manufacturer" -> stockService.updateProduct(null, catalogNum, newValue, -1, -1, branch);
                case "store price" -> stockService.updateProduct(null, catalogNum, null, Integer.parseInt(newValue), -1, branch);
                default -> {
                    JOptionPane.showMessageDialog(inputFrame, "Invalid command", "Error", JOptionPane.ERROR_MESSAGE);
                    inputFrame.dispose();
                    return;
                }
            }

            JOptionPane.showMessageDialog(inputFrame, "Product updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

    private void updateItem() {
        JFrame inputFrame = new JFrame("Update Item");
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel catalogLabel = new JLabel("Catalog Number:");
        JTextField catalogTextField = new JTextField(10);
        JLabel serialLabel = new JLabel("Serial Number:");
        JTextField serialTextField = new JTextField(10);
        JLabel optionLabel = new JLabel("Choose details to update:");
        JComboBox<String> optionComboBox = new JComboBox<>(new String[]{"Is Defective?", "Is Sold?", "Supplier ID", "Supplier Price", "Supplier Discount", "Sold Price", "Location"});

        JLabel newValLabel = new JLabel("New Value:");
        JTextField newValTextField = new JTextField(10);

        panel.add(catalogLabel);
        panel.add(catalogTextField);
        panel.add(serialLabel);
        panel.add(serialTextField);
        panel.add(optionLabel);
        panel.add(optionComboBox);
        panel.add(newValLabel);
        panel.add(newValTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Update Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String catalogNum = catalogTextField.getText();
            String serialNum = serialTextField.getText();
            int optionIndex = optionComboBox.getSelectedIndex();
            String newValue = newValTextField.getText();

            switch (optionIndex) {
                case 0:
                    stockService.updateProduct(Integer.parseInt(newValue), catalogNum, serialNum, -1, null, -1, -1, -1, null, branch);
                    break;
                case 1:
                    stockService.updateProduct(-1, catalogNum, serialNum, Integer.parseInt(newValue), null, -1, -1, -1, null, branch);
                    break;
                case 2:
                    stockService.updateProduct(-1, catalogNum, serialNum, -1, newValue, -1, -1, -1, null, branch);
                    break;
                case 3:
                    stockService.updateProduct(-1, catalogNum, serialNum, -1, null, Double.parseDouble(newValue), -1, -1, null, branch);
                    break;
                case 4:
                    stockService.updateProduct(-1, catalogNum, serialNum, -1, null, -1, Double.parseDouble(newValue), -1, null, branch);
                    break;
                case 5:
                    stockService.updateProduct(-1, catalogNum, serialNum, -1, null, -1, -1, Integer.parseInt(newValue), null, branch);
                    break;
                case 6:
                    stockService.updateProduct(-1, catalogNum, serialNum, -1, null, -1, -1, -1, newValue, branch);
                    break;
                default:
                    System.out.println("\nInvalid command");
                    break;
            }
        }

        inputFrame.dispose();
    }

    private void getProductDetails() {
        JFrame inputFrame = new JFrame("Get Product Details");
        JPanel panel = new JPanel(new GridLayout(2, 2));

        JLabel catalogLabel = new JLabel("Catalog Number:");
        JTextField catalogTextField = new JTextField(10);
        JLabel serialLabel = new JLabel("Serial Number:");
        JTextField serialTextField = new JTextField(10);

        panel.add(catalogLabel);
        panel.add(catalogTextField);
        panel.add(serialLabel);
        panel.add(serialTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Get Product Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String catalogNum = catalogTextField.getText();
            String serialNum = serialTextField.getText();

            Response response = stockService.getProductDetails(catalogNum, serialNum, branch);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Product Details", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

    public static void main(String[] args) {
        new ProductsMenu().run("branch1");
    }
}
