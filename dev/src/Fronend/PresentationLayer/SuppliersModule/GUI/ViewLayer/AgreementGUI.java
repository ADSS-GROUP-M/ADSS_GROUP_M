package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.SuppliersModule.Agreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryFixedDays;
import Backend.BusinessLayer.SuppliersModule.Product;
import Backend.ServiceLayer.SuppliersModule.AgreementService;
import Backend.ServiceLayer.SuppliersModule.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AgreementGUI extends JFrame {
    private AgreementService agreementService = new AgreementService();
    private Gson gson = new Gson();
    private DeliveryAgreement deliveryAgreement;

    private Map<String, Product> products;
    private JLabel deliveryLabel;
    private JLabel deliveryDescriptionLabel;
    private JPanel deliveryPanel;
    private JTable productTable;
    private String bnNumber;

    public AgreementGUI(String bnNumber) {
        this.bnNumber = bnNumber;
        java.lang.reflect.Type responseOfAgreement = new TypeToken<Response<Agreement>>(){}.getType();
        Response<Agreement> r = gson.fromJson(agreementService.getAgreement(bnNumber), responseOfAgreement);
        this.products = r.getReturnValue().getProducts();
        this.deliveryAgreement = r.getReturnValue().getDeliveryAgreement();
        // Set up the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.setTitle("Agreement GUI");

        // Create the delivery agreement panel
        deliveryPanel = new JPanel(new BorderLayout());
        deliveryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel agreementLabel = new JLabel("Delivery Agreement");
        agreementLabel.setFont(new Font("Arial", Font.BOLD, 16));
        deliveryPanel.add(agreementLabel, BorderLayout.NORTH);

        deliveryDescriptionLabel = new JLabel();
        deliveryDescriptionLabel.setText(deliveryAgreement.toString3());
        deliveryDescriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        JScrollPane descriptionScrollPane = new JScrollPane(deliveryDescriptionLabel);
        deliveryPanel.add(descriptionScrollPane, BorderLayout.CENTER);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeliveryAgreement d = showDeliveryAgreementDialog();
                if(d == null)
                    JOptionPane.showMessageDialog(null,"No delivery agreement was selected", "error", JOptionPane.ERROR_MESSAGE);
                else {
                    Response r;
                    if (d instanceof DeliveryFixedDays)
                        r = gson.fromJson(agreementService.setFixedDeliveryAgreement(bnNumber, d.getHavaTransport(), ((DeliveryFixedDays) d).getDaysOfTheWeek()), Response.class);
                    else
                        r = gson.fromJson(agreementService.setByInvitationDeliveryAgreement(bnNumber, d.getHavaTransport(), ((DeliveryByInvitation) d).getNumberOfDays()), Response.class);
                    if (r.errorOccurred())
                        JOptionPane.showMessageDialog(null, r.getError(), "error", JOptionPane.ERROR_MESSAGE);
                    else {
                        JOptionPane.showMessageDialog(null, r.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
                        deliveryAgreement = d;
                        refreshDeliveryPanel();
                    }
                }
            }
        });
        deliveryPanel.add(editButton, BorderLayout.SOUTH);

        mainPanel.add(deliveryPanel, BorderLayout.NORTH);

        // Create the product table
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Catalog Number", "Supplier's Catalog Number", "Price", "Number of Units"}, 0);

        productTable = new JTable(tableModel) {
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Populate the table with product data
        for (Product product : products.values()) {
            tableModel.addRow(new Object[]{
                    product.getCatalogNumber(),
                    product.getSuppliersCatalogNumber(),
                    product.getPrice(),
                    product.getNumberOfUnits()
            });
        }

        // Create the "Add Product" button
        JButton addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        mainPanel.add(addProductButton, BorderLayout.SOUTH);
        // Create the "Remove Selected Row" button
        JButton removeRowButton = new JButton("Remove Selected Row");
        removeRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedRow();
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(removeRowButton, BorderLayout.SOUTH);

        // Add the panel and the "Add Discount" button to the main frame
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(addProductButton, BorderLayout.SOUTH);

        // Set initial visibility and size of the GUI window
        this.setVisible(true);
        this.setBounds(330, 150, 600, 400);
    }

    private void removeSelectedRow() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String catalogNumber = productTable.getValueAt(selectedRow, 0).toString();
            Response r = gson.fromJson(agreementService.removeProduct(bnNumber, catalogNumber), Response.class);
            if(r.errorOccurred())
                JOptionPane.showMessageDialog(null, r.getError(), "error", JOptionPane.ERROR_MESSAGE);
            else {
                JOptionPane.showMessageDialog(null, r.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
                // remove the row
                DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
                tableModel.removeRow(selectedRow);
            }
        }
    }

    private void refreshDeliveryPanel(){
        deliveryDescriptionLabel.setText(deliveryAgreement.toString3());
        // Refresh the panel
        deliveryPanel.revalidate();
        deliveryPanel.repaint();
    }

    private DeliveryAgreement showDeliveryAgreementDialog() {
        // Options for the "Does he have a transport?" question
        String[] transportOptions = {"Yes", "No"};
        String transportChoice = (String) JOptionPane.showInputDialog(this,
                "Does he have a transport?", "Delivery Agreement",
                JOptionPane.QUESTION_MESSAGE, null, transportOptions, transportOptions[0]);

        if (transportChoice == null) {
            // User canceled the dialog
            return null;
        }

        // Options for the "Does he arrive in fixed days?" question
        String[] fixedDaysOptions = {"Yes", "No"};
        String fixedDaysChoice = (String) JOptionPane.showInputDialog(this,
                "Does he arrive in fixed days?", "Delivery Agreement",
                JOptionPane.QUESTION_MESSAGE, null, fixedDaysOptions, fixedDaysOptions[0]);

        if (fixedDaysChoice == null) {
            // User canceled the dialog
            return null;
        }

        if (fixedDaysChoice.equals("Yes")) {
            // Options for selecting days of the week
            java.util.List<String> days = new LinkedList<>();
            boolean cond = true;
            while (cond) {
                String[] daysOfWeekOptions = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                String selectedDaysOfWeek = (String) JOptionPane.showInputDialog(this,
                        "Select days of the week:", "Delivery Agreement",
                        JOptionPane.QUESTION_MESSAGE, null, daysOfWeekOptions, null);
                cond = selectedDaysOfWeek != null;
                if(cond)
                    days.add(selectedDaysOfWeek);
            }

            // Do something with the selected days of the week
            Set<Integer> daysNoDup = new HashSet<>();
            for (String day : days) {
                // Process each selected day
                switch (day){
                    case "Sunday":
                        daysNoDup.add(0);
                        break;
                    case "Monday":
                        daysNoDup.add(1);
                        break;
                    case "Tuesday":
                        daysNoDup.add(2);
                        break;
                    case "Wednesday":
                        daysNoDup.add(3);
                        break;
                    case "Thursday":
                        daysNoDup.add(4);
                        break;
                    case "Friday":
                        daysNoDup.add(5);
                        break;
                    case "Saturday":
                        daysNoDup.add(6);
                        break;
                }
            }
            List<Integer> daysOfTheWeek = daysNoDup.stream().toList();
            return new DeliveryFixedDays(transportChoice.equals("yes"), daysOfTheWeek);
        } else {
            // Option for entering number of days for arrival
            String numOfDays = JOptionPane.showInputDialog(this,
                    "Enter number of days for arrival:", "Delivery Agreement",
                    JOptionPane.QUESTION_MESSAGE);

            if (numOfDays == null) {
                // User canceled the dialog
                return null;
            }

            // Convert the input to an integer and do something with it
            int days = Integer.parseInt(numOfDays);
            // Process the entered number of days
            return new DeliveryByInvitation(transportChoice.equals("yes"), days);
        }
    }

    private void addProduct() {
        // Show a dialog to input product details
        JTextField catalogNumberField = new JTextField();
        JTextField suppliersCatalogNumberField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField numberOfUnitsField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Catalog Number:"));
        inputPanel.add(catalogNumberField);
        inputPanel.add(new JLabel("Supplier's Catalog Number:"));
        inputPanel.add(suppliersCatalogNumberField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Number of Units:"));
        inputPanel.add(numberOfUnitsField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Add Product", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Get the input values
            String catalogNumber = catalogNumberField.getText();
            String suppliersCatalogNumber = suppliersCatalogNumberField.getText();
            double price = Double.parseDouble(priceField.getText());
            int numberOfUnits = Integer.parseInt(numberOfUnitsField.getText());
            Response r = gson.fromJson(agreementService.addProduct(bnNumber, catalogNumber, suppliersCatalogNumber, price, numberOfUnits), Response.class);
            if(r.errorOccurred())
                JOptionPane.showMessageDialog(null, r.getError(), "error", JOptionPane.ERROR_MESSAGE);
            else {
                JOptionPane.showMessageDialog(null, r.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
                // Add the product to the table
                DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
                tableModel.addRow(new Object[]{catalogNumber, suppliersCatalogNumber, price, numberOfUnits});
            }

        }
    }

    public static void main(String[] args) {
        // Create sample products map for testing
        Map<String, Product> products = Map.of(
                "1", new Product("1", "A001", 9.99, 50),
                "2", new Product("2", "B002", 19.99, 100),
                "3", new Product("3", "C003", 14.99, 75)
        );

        SwingUtilities.invokeLater(() -> new AgreementGUI("1"));
    }
}
