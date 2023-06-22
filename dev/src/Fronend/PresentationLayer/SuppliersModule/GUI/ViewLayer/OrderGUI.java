package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.ServiceLayer.SuppliersModule.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class OrderGUI extends JFrame {
    private Map<String, Integer> products;
    private OrderService orderService = new OrderService();

    public OrderGUI(Map<String, Integer> products) {
        this.products = products;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Order Details");

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Create the table model with columns for "Catalog Number" and "Quantity"
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Catalog Number", "Quantity"}, 0);

        // Populate the table model with data from the products map
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String catalogNumber = entry.getKey();
            int quantity = entry.getValue();
            tableModel.addRow(new Object[]{catalogNumber, quantity});
        }

        // Create the JTable with the populated table model
        JTable productTable = new JTable(tableModel);

        // Create a scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Add the scroll pane to the main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Set the size and visibility of the frame
        this.setBounds(400, 150, 600, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Create a sample products map for testing
        Map<String, Integer> products = new HashMap<>();
        products.put("P1", 10);
        products.put("P2", 5);
        products.put("P3", 8);

        SwingUtilities.invokeLater(() -> new OrderGUI(products));
    }
}
