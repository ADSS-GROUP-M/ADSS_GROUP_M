package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.SuppliersModule.BillOfQuantities;
import Backend.BusinessLayer.SuppliersModule.Discounts.CashDiscount;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Discounts.PercentageDiscount;
import Backend.ServiceLayer.SuppliersModule.BillOfQuantitiesService;
import Backend.ServiceLayer.SuppliersModule.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DiscountTable extends JFrame {
    private JTable table;
    private Gson gson = new Gson();
    private BillOfQuantitiesService billOfQuantitiesService = new BillOfQuantitiesService();
    private String bnNumber;

    public DiscountTable(String bnNumber) {
        this.bnNumber = bnNumber;
        java.lang.reflect.Type responseOfBOQ = new TypeToken<Response<BillOfQuantities>>(){}.getType();
        Map<String, Map<Integer, Discount>> productsDiscounts = ((Response<BillOfQuantities>)gson.fromJson(billOfQuantitiesService.getBillOfQuantities(bnNumber), responseOfBOQ)).getReturnValue().getProductsDiscounts();
        setTitle("Discount Table");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create the table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Catalog Number");
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
        table = new JTable(model) {
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create the "Add Discount" button
        JButton addDiscountButton = new JButton("Add Product Discount");
        addDiscountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDiscount();
            }
        });

        // Create the "Remove Selected Row" button
        JButton removeRowButton = new JButton("Remove Selected Row");
        removeRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedRow();
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(removeRowButton, BorderLayout.SOUTH);

        // Add the panel and the "Add Discount" button to the main frame
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(addDiscountButton, BorderLayout.SOUTH);

        this.setBounds(330, 150, 1100, 550);
        setPreferredSize(new Dimension(3000, 500));
    }

    private void removeSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String catalogNumber = table.getValueAt(selectedRow, 0).toString();
            int quantity = Integer.parseInt(table.getValueAt(selectedRow, 1).toString());
            Response r = gson.fromJson(billOfQuantitiesService.removeProductDiscount(bnNumber, catalogNumber, quantity), Response.class);
            if(r.errorOccurred())
                JOptionPane.showMessageDialog(null, r.getError(), "error", JOptionPane.ERROR_MESSAGE);
            else {
                JOptionPane.showMessageDialog(null, r.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
                // remove the row
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                tableModel.removeRow(selectedRow);
            }
        }
    }

    private Discount chooseDiscount(){
        String[] discountOptions = { "Cash Discount", "Percentage Discount" };
        String selectedOption = (String) JOptionPane.showInputDialog(this, "Choose the discount type:", "choose discount",
                JOptionPane.PLAIN_MESSAGE, null, discountOptions, discountOptions[0]);
        if (selectedOption != null) {
            if (selectedOption.equals("Cash Discount")) {
                String inputValue = JOptionPane.showInputDialog(this, "Enter the amount of discount:");
                try {
                    double amount = Double.parseDouble(inputValue);
                    return new CashDiscount(amount);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid double.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (selectedOption.equals("Percentage Discount")) {
                String inputValue = JOptionPane.showInputDialog(this, "Enter the percentage of discount:");
                try {
                    double percentage = Double.parseDouble(inputValue);
                    return new PercentageDiscount(percentage);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid double.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return null;
    }
    //so can be used in lambda as actionListener
    private Discount discount;
    private void addDiscount() {
        // Show a dialog to input discount details
        JTextField productField = new JTextField();
        JTextField quantityField = new JTextField();
        JButton discountButton = new JButton("add discount");
        discountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                discount = chooseDiscount();
            }
        });
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Catalog Number:"));
        inputPanel.add(productField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Discount:"));
        inputPanel.add(discountButton);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Add Discount", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && discount != null) {
            // Get the input values
            String catalogNumber = productField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            Response r = gson.fromJson(billOfQuantitiesService.setProductDiscount(bnNumber, catalogNumber, quantity, discount), Response.class);
            if(r.errorOccurred())
                JOptionPane.showMessageDialog(null, r.getError(), "error", JOptionPane.ERROR_MESSAGE);
            else {
                JOptionPane.showMessageDialog(null, r.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
                // Add the discount to the table
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                tableModel.addRow(new Object[]{catalogNumber, quantity, discount});
            }
        }
        discount = null;
    }

    public static void main(String[] args){
        // Example usage
        Map<String, Map<Integer, Discount>> productsDiscounts = new HashMap<>(){{put("1", new HashMap<>(){{put(1, new CashDiscount(10));
        put(3, new PercentageDiscount(3));}});
        put("123", new HashMap<>(){{put(3, new PercentageDiscount(23)); put(10, new PercentageDiscount(1));}});}};

        SwingUtilities.invokeLater(() -> {
            DiscountTable discountTable = new DiscountTable("1");
            discountTable.setVisible(true);
        });
    }
    }

