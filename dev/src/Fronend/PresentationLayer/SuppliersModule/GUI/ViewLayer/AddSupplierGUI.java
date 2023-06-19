package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.SuppliersModule.BankAccount;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryFixedDays;
import Backend.BusinessLayer.SuppliersModule.Pair;
import Backend.BusinessLayer.SuppliersModule.Product;
import Backend.ServiceLayer.SuppliersModule.Response;
import Backend.ServiceLayer.SuppliersModule.SupplierService;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AddSupplierGUI extends JFrame {
    private DeliveryAgreement deliveryAgreement;
    private Gson gson = new Gson();
    private SupplierService supplierService = new SupplierService();
    private JPanel mainPanel;
    private JTextField nameTextField;
    private JTextField bnNumberTextField;
    private JTextField bankTextField;
    private JTextField branchTextField;
    private JTextField accountNumberTextField;
    private JTextField paymentMethodTextField;
    private JTextField fieldTextField;
    private JTextField contactNameTextField;
    private JTextField emailTextField;
    private JTextField phoneNumberTextField;
    private JButton addProductButton;
    private JButton addSupplierButton;
    private JPanel productsPanel;
    private JCheckBox transportCheckBox;
    private JCheckBox fixedDaysCheckBox;
    private JTextField dayTextField;
    private JTextField numOfDaysTextField;
    private JButton addDeliveryAgreementButton;
    private JComboBox<String> paymentMethodComboBox;
    private JPanel supplierPanel;


    private List<JTextField> catalogNumberTextFields;
    private List<JTextField> supplierCatalogNumberTextFields;
    private List<JTextField> priceTextFields;
    private List<JTextField> unitsTextFields;

    public AddSupplierGUI() {
        // Set up the main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setTitle("Add Supplier");

        // Create the supplier form panel
        supplierPanel = new JPanel();
        supplierPanel.setLayout(new GridLayout(0, 2));

        // Add supplier form fields
        supplierPanel.add(new JLabel("Name:"));
        nameTextField = new JTextField();
        supplierPanel.add(nameTextField);

        supplierPanel.add(new JLabel("BN Number:"));
        bnNumberTextField = new JTextField();
        supplierPanel.add(bnNumberTextField);

        supplierPanel.add(new JLabel("Bank:"));
        bankTextField = new JTextField();
        supplierPanel.add(bankTextField);

        supplierPanel.add(new JLabel("Branch:"));
        branchTextField = new JTextField();
        supplierPanel.add(branchTextField);

        supplierPanel.add(new JLabel("Account Number:"));
        accountNumberTextField = new JTextField();
        supplierPanel.add(accountNumberTextField);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        supplierPanel.add(paymentMethodLabel);

        String[] paymentOptions = {"Net", "Net 30 EOM", "Net 60 EOM"};
        paymentMethodComboBox = new JComboBox<>(paymentOptions);
        supplierPanel.add(paymentMethodComboBox);

        supplierPanel.add(new JLabel("Fields (comma-separated):"));
        fieldTextField = new JTextField();
        supplierPanel.add(fieldTextField);

        supplierPanel.add(new JLabel("Contact Name:"));
        contactNameTextField = new JTextField();
        supplierPanel.add(contactNameTextField);

        supplierPanel.add(new JLabel("Email:"));
        emailTextField = new JTextField();
        supplierPanel.add(emailTextField);

        supplierPanel.add(new JLabel("Phone Number:"));
        phoneNumberTextField = new JTextField();
        supplierPanel.add(phoneNumberTextField);

        mainPanel.add(supplierPanel, BorderLayout.NORTH);

        // Create the products panel
        productsPanel = new JPanel();
        productsPanel.setLayout(new GridLayout(0, 2));

        supplierPanel.add(new JLabel("Add Delivery Agreement:"));
        addDeliveryAgreementButton = new JButton("Add Delivery Agreement");
        supplierPanel.add(addDeliveryAgreementButton);

// Action listener for the "Add Delivery Agreement" button
        addDeliveryAgreementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deliveryAgreement = showDeliveryAgreementDialog();
            }
        });


        // Initialize the lists for storing product text fields
        catalogNumberTextFields = new ArrayList<>();
        supplierCatalogNumberTextFields = new ArrayList<>();
        priceTextFields = new ArrayList<>();
        unitsTextFields = new ArrayList<>();

        mainPanel.add(new JScrollPane(productsPanel), BorderLayout.CENTER);

        // Create the product button panel
        JPanel buttonPanel = new JPanel();
        addProductButton = new JButton("Add Product");
        addSupplierButton = new JButton("Add Supplier");
        buttonPanel.add(addProductButton);
        buttonPanel.add(addSupplierButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Register action listeners
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductRow();
            }
        });

        addSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });

        // Set initial visibility and size of the GUI window
        this.setVisible(true);
        this.setBounds(330, 150, 865, 550);
    }

    private void addProductRow() {
        // Create product row components
        JTextField catalogNumberTextField = new JTextField();
        JTextField supplierCatalogNumberTextField = new JTextField();
        JTextField priceTextField = new JTextField();
        JTextField unitsTextField = new JTextField();

        // Add components to the products panel
        productsPanel.add(new JLabel("Catalog Number:"));
        productsPanel.add(catalogNumberTextField);

        productsPanel.add(new JLabel("Supplier Catalog Number:"));
        productsPanel.add(supplierCatalogNumberTextField);

        productsPanel.add(new JLabel("Price:"));
        productsPanel.add(priceTextField);

        productsPanel.add(new JLabel("Units:"));
        productsPanel.add(unitsTextField);

        // Add text fields to the respective lists
        catalogNumberTextFields.add(catalogNumberTextField);
        supplierCatalogNumberTextFields.add(supplierCatalogNumberTextField);
        priceTextFields.add(priceTextField);
        unitsTextFields.add(unitsTextField);

        // Refresh the panel
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void addSupplier() {
        // Retrieve entered data from the text fields
        String name = nameTextField.getText();
        String bnNumber = bnNumberTextField.getText();
        String bank = bankTextField.getText();
        String branch = branchTextField.getText();
        String accountNumber = accountNumberTextField.getText();
        String paymentMethod =  (String) paymentMethodComboBox.getSelectedItem();;
        String fields = fieldTextField.getText();
        String contactName = contactNameTextField.getText();
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();

        List<Product> products = new LinkedList<>();
        for (int i = 0; i < catalogNumberTextFields.size(); i++) {
            products.add(new Product(catalogNumberTextFields.get(i).getText(), supplierCatalogNumberTextFields.get(i).getText(), Double.parseDouble(priceTextFields.get(i).getText()), Integer.parseInt(unitsTextFields.get(i).getText())));
        }


        Response r = gson.fromJson(supplierService.addSupplier(name, bnNumber, new BankAccount(bank, branch, accountNumber), paymentMethod, Arrays.asList(fields.split(",")),
                new HashMap<>(){{put(email, new Pair<>(contactName, phoneNumber));}}, products, deliveryAgreement), Response.class);
        if(r.errorOccurred())
            JOptionPane.showMessageDialog(null, r.getError(), "error", JOptionPane.ERROR_MESSAGE);
        else {
            JOptionPane.showMessageDialog(null, r.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
            // Clear the form
            clearForm();
        }

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
            List<String> days = new LinkedList<>();
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


    private void clearForm() {
        nameTextField.setText("");
        bnNumberTextField.setText("");
        bankTextField.setText("");
        branchTextField.setText("");
        accountNumberTextField.setText("");
        paymentMethodTextField.setText("");
        fieldTextField.setText("");
        contactNameTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");

        // Clear product text fields and remove from panel
        for (JTextField textField : catalogNumberTextFields) {
            productsPanel.remove(textField);
        }
        for (JTextField textField : supplierCatalogNumberTextFields) {
            productsPanel.remove(textField);
        }
        for (JTextField textField : priceTextFields) {
            productsPanel.remove(textField);
        }
        for (JTextField textField : unitsTextFields) {
            productsPanel.remove(textField);
        }

        // Clear the product lists
        catalogNumberTextFields.clear();
        supplierCatalogNumberTextFields.clear();
        priceTextFields.clear();
        unitsTextFields.clear();
        productsPanel.removeAll();

        // Refresh the panel
        mainPanel.revalidate();
        mainPanel.repaint();
        supplierPanel.revalidate();
        supplierPanel.repaint();
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddSupplierGUI();
            }
        });
    }
}
