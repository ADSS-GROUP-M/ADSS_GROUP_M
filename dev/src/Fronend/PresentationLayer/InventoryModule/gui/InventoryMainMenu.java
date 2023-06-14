package Fronend.PresentationLayer.InventoryModule.gui;
import Backend.ServiceLayer.InventoryModule.CategoriesService;
import Backend.ServiceLayer.InventoryModule.StockService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryMainMenu {
    protected StockService stockService;
    protected CategoriesService categoriesService;

    public InventoryMainMenu(){
        stockService = new StockService();
        categoriesService = new CategoriesService();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel labelPanel = new JPanel(); // New panel for labels
        JLabel mainLabel = new JLabel("Welcome to the Inventory GUI!", JLabel.CENTER);
        JLabel secondLabel = new JLabel("Please choose branch and menu");
        JPanel panel = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);
        panel.setBorder(new EmptyBorder(45, 70, 45, 70));

        // Create a new panel for the branch input
        JPanel branchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel branchLabel = new JLabel("Branch:");
        JTextField branchTextField = new JTextField(10);
        branchPanel.add(branchLabel);
        branchPanel.add(branchTextField);

        // Define new buttons
        JButton categoriesMenu = new JButton("Categories Menu");
        JButton discountsMenu = new JButton("Discounts Menu");
        JButton productsMenu = new JButton("Products Menu");
        JButton reportsMenu = new JButton("Reports Menu");

        // Add buttons to the panel (and spaces between buttons)
        panel.add(categoriesMenu);
        panel.add(discountsMenu);
        panel.add(productsMenu);
        panel.add(reportsMenu);

        // Set vertical alignment for the second label
        secondLabel.setVerticalAlignment(JLabel.CENTER);

        // Set font size for mainLabel
        mainLabel.setFont(new Font(mainLabel.getFont().getName(), Font.PLAIN, 20));

        // Add the labels to the label panel
        labelPanel.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 20, 0);
        labelPanel.add(mainLabel, constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 10, 0);
        labelPanel.add(secondLabel, constraints);

        // Add the branch panel and label panel to the frame
        frame.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 10, 0);
        frame.add(labelPanel, constraints);
        constraints.gridy = 1;
        frame.add(branchPanel, constraints);
        constraints.gridy = 2;
        frame.add(panel, constraints);

        productsMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String branch = branchTextField.getText();
                ProductsMenu menu = new ProductsMenu();
                menu.run(branch);
                frame.dispose();
            }
        });

        categoriesMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String branch = branchTextField.getText();
                CategoriesMenu menu = new CategoriesMenu();
                menu.run(branch);
                frame.dispose();
            }
        });

        discountsMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String branch = branchTextField.getText();
                DiscountsMenu menu = new DiscountsMenu();
                menu.run(branch);
                frame.dispose();
            }
        });

        reportsMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String branch = branchTextField.getText();
                ReportsMenu menu = new ReportsMenu();
                menu.run(branch);
                frame.dispose();
            }
        });

        // Settings for the frame
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
