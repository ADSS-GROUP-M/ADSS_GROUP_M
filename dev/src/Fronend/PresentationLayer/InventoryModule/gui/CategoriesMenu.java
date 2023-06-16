package Fronend.PresentationLayer.InventoryModule.gui;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.ServiceLayer.InventoryModule.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesMenu extends InventoryMainMenu{
    private String branch;
    private JFrame frame;

    public void run(String branch) {
        this.branch = branch;
        frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setSize(600, 400); // Set the size of the Categories Menu frame
        panel.setLayout(new GridLayout(7, 1));

        JLabel titleLabel = new JLabel("Please select an option:");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton createCategoryButton = new JButton("Add new category");
        JButton removeCategoryButton = new JButton("Remove category");
        JButton addProductToCategoryButton = new JButton("Add product to category");
        JButton removeProductFromCategoryButton = new JButton("Remove product from category");
        JButton backButton = new JButton("Back to Main Menu");

        createCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCategory();
            }
        });

        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCategory();
            }
        });

        addProductToCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProductToCategory();
            }
        });

        removeProductFromCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeProductFromCategory();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InventoryMainMenu.main(new String[0]);
                frame.dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(createCategoryButton);
        panel.add(removeCategoryButton);
        panel.add(addProductToCategoryButton);
        panel.add(removeProductFromCategoryButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createCategory() {
        JFrame inputFrame = new JFrame("Create New Category");
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel categoryNameLabel = new JLabel("Category Name:");
        JTextField categoryNameTextField = new JTextField(10);
        JLabel subcategoriesLabel = new JLabel("Subcategories (comma-separated):");
        JTextField subcategoriesTextField = new JTextField(10);

        panel.add(categoryNameLabel);
        panel.add(categoryNameTextField);
        panel.add(subcategoriesLabel);
        panel.add(subcategoriesTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Create New Category", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameTextField.getText();
            String subcategoriesText = subcategoriesTextField.getText();
            List<String> subcategories;
            if (subcategoriesText.isEmpty()) {
                subcategories = new ArrayList<>(); // Creating an empty ArrayList
            } else {
                subcategories = Arrays.asList(subcategoriesText.split(","));
            }

            // Call the method to create a new category with the obtained values
            Response response = categoriesService.createCategory(Branch.valueOf(branch), subcategories, categoryName);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }


    private void removeCategory() {
        JFrame inputFrame = new JFrame("Remove Category");
        JPanel panel = new JPanel(new GridLayout(2, 2));

        JLabel categoryNameLabel = new JLabel("Category Name:");
        JTextField categoryNameTextField = new JTextField(10);

        panel.add(categoryNameLabel);
        panel.add(categoryNameTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Remove Category", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameTextField.getText();

            // Call the method to remove the category with the obtained category name
            Response response = categoriesService.removeCategory(Branch.valueOf(branch), categoryName);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

    private void addProductToCategory() {
        JFrame inputFrame = new JFrame("Add Product to Category");
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel categoryNameLabel = new JLabel("Category Name:");
        JTextField categoryNameTextField = new JTextField(10);
        JLabel catalogNumberLabel = new JLabel("Catalog Number:");
        JTextField catalogNumberTextField = new JTextField(10);

        panel.add(categoryNameLabel);
        panel.add(categoryNameTextField);
        panel.add(catalogNumberLabel);
        panel.add(catalogNumberTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Add Product to Category", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameTextField.getText();
            String catalogNumber = catalogNumberTextField.getText();

            // Call the method to add the product to the category with the obtained values
            Response response = categoriesService.addProductToCategory(Branch.valueOf(branch), catalogNumber, categoryName);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

    private void removeProductFromCategory() {
        JFrame inputFrame = new JFrame("Remove Product from Category");
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel categoryNameLabel = new JLabel("Category Name:");
        JTextField categoryNameTextField = new JTextField(10);
        JLabel catalogNumberLabel = new JLabel("Catalog Number:");
        JTextField catalogNumberTextField = new JTextField(10);

        panel.add(categoryNameLabel);
        panel.add(categoryNameTextField);
        panel.add(catalogNumberLabel);
        panel.add(catalogNumberTextField);

        int result = JOptionPane.showConfirmDialog(inputFrame, panel, "Remove Product from Category", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameTextField.getText();
            String catalogNumber = catalogNumberTextField.getText();

            // Call the method to remove the product from the category with the obtained values
            Response response = categoriesService.removeProductFromCategory(Branch.valueOf(branch), categoryName, catalogNumber);
            if (response.errorOccurred())
                JOptionPane.showMessageDialog(inputFrame, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(inputFrame, response.getReturnValue(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        inputFrame.dispose();
    }

}

