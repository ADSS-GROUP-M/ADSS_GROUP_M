package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.SuppliersModule.Discounts.CashDiscount;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Discounts.PercentageDiscount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DiscountTable extends JFrame {
    private JTable table;

    public DiscountTable(Map<String, Map<Integer, Discount>> productsDiscounts) {
        setTitle("Discount Table");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create the table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product");
        model.addColumn("Quantity");
        model.addColumn("Discount");

        // Populate the table model with data
        for (String product : productsDiscounts.keySet()) {
            Map<Integer, Discount> discounts = productsDiscounts.get(product);
            for (Integer quantity : discounts.keySet()) {
                Discount discount = discounts.get(quantity);
                model.addRow(new Object[]{product, quantity, discount.toString()});
            }
        }

        // Create the table with the model
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.setBounds(330, 150, 900, 550);
        setPreferredSize(new Dimension(3000, 500));
    }

    public static void main(String[] args) {
        // Example usage
        Map<String, Map<Integer, Discount>> productsDiscounts = new HashMap<>(){{put("1", new HashMap<>(){{put(1, new CashDiscount(10));
        put(3, new PercentageDiscount(3));}});
        put("123", new HashMap<>(){{put(3, new PercentageDiscount(23)); put(10, new PercentageDiscount(1));}});}};

        SwingUtilities.invokeLater(() -> {
            DiscountTable discountTable = new DiscountTable(productsDiscounts);
            discountTable.setVisible(true);
        });
    }
}
