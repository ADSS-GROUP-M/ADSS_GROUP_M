package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.SuppliersModule.BankAccount;
import Backend.BusinessLayer.SuppliersModule.BillOfQuantities;
import Backend.BusinessLayer.SuppliersModule.Supplier;
import Backend.ServiceLayer.SuppliersModule.BillOfQuantitiesService;
import Backend.ServiceLayer.SuppliersModule.Response;
import Fronend.PresentationLayer.SuppliersModule.GUI.ControllersLayer.ManageSuppliersController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.function.Function;

public class SupplierPage extends JFrame {
    private Gson gson = new Gson();
    private JLabel picture;
    private JLabel name;
    private JLabel bnNumber;
    private JLabel bankAccount;
    private JLabel fields;
    private JLabel paymentMethod;
    private BackgruondPanel jpanel;
    private JButton showAgreementButton;
    private JButton showBillButton;
    private BillOfQuantitiesService billOfQuantitiesService = new BillOfQuantitiesService();

    public SupplierPage() {
    }

    public void initPage(Supplier supplier, ManageSuppliersController manageSuppliersController) {
        jpanel = new BackgruondPanel("src/Fronend/PresentationLayer/SuppliersModule/GUI/ViewLayer/resource/ManageSupplier.png");
        jpanel.setBounds(0, 0, 850, 550);
        jpanel.setLayout(null);

        // Image
        ImageIcon blankPerson = new ImageIcon(new ImageIcon("src/Fronend/PresentationLayer/SuppliersModule/GUI/ViewLayer/resource/blankPerson.png").getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
        picture = new JLabel();
        picture.setIcon(blankPerson);
        picture.setBounds(50, 50, 300, 200);

        // Name
        name = createEditableLabel("NAME: " + supplier.getName(), 260, 50, 500, 50, (String value) -> manageSuppliersController.setName(supplier.getBnNumber(), value));

        // BN number
        bnNumber = createEditableLabel("BN NUMBER: " + supplier.getBnNumber(), 260, 110, 300, 50, (String value) -> manageSuppliersController.setBnNumber(supplier.getBnNumber(), value));

        // Bank account
        BankAccount bankAccountObj = supplier.getBankAccount();
        String bankAccountText = "BANK: " + bankAccountObj.getBank() + ", " + bankAccountObj.getBranch() + ", " + bankAccountObj.getAccountNumber();
        bankAccount = createEditableLabel(bankAccountText, 260, 170, 500, 50, (String value) -> manageSuppliersController.setBankAccount(supplier.getBnNumber(), value));

        // Fields
        StringBuilder fieldsText = new StringBuilder("FIELDS: ");
        for (String field : supplier.getFields()) {
            fieldsText.append(field).append(", ");
        }
        fieldsText.setLength(fieldsText.length() - 2);
        fields = createEditableLabel(fieldsText.toString(), 260, 230, 300, 50, (String value) -> true);

        // Payment method
        paymentMethod = createEditableLabel("PAYMENT METHOD: " + supplier.getPaymentMethod(), 260, 290, 300, 50, (String value) -> manageSuppliersController.setPaymentMethod(supplier.getBnNumber(), value));

        // Show Agreement button
        showAgreementButton = createButton("Show Agreement", 330, 380, 180, 30);
        showAgreementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the "Show Agreement" button click
                new AgreementGUI(supplier.getBnNumber());
            }
        });

        // Show Bill of Quantities button
        showBillButton = createButton("Show Bill of Quantities", 520, 380, 180, 30);
        showBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the "Show Bill of Quantities" button click
                java.lang.reflect.Type responseOfBOQ = new TypeToken<Response<BillOfQuantities>>(){}.getType();
                Response<BillOfQuantities> r = gson.fromJson(billOfQuantitiesService.getBillOfQuantities(supplier.getBnNumber()), responseOfBOQ);
                if(r.errorOccurred())
                    JOptionPane.showMessageDialog(null, r.getError(), "Not found", JOptionPane.INFORMATION_MESSAGE);
                else {
                    JFrame f = new JFrame("Bill of Quantities");
                    f.add(new BillOfQuantitiesPage(supplier.getBnNumber()));
                    f.pack();
                    f.setVisible(true);
                }
            }
        });

        jpanel.add(picture);
        jpanel.add(name);
        jpanel.add(bnNumber);
        jpanel.add(fields);
        jpanel.add(paymentMethod);
        jpanel.add(bankAccount);
        jpanel.add(showAgreementButton);
        jpanel.add(showBillButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setBounds(330, 150, 850, 550);
        setVisible(true);
        add(jpanel);
    }

    private JLabel createEditableLabel(String text, int x, int y, int width, int height, Function<String, Boolean> function) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font(null, Font.PLAIN, 20));

        JTextField textField = new JTextField(text);
        textField.setBounds(x, y, width, height);
        textField.setVisible(false);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (function.apply(textField.getText().split(":")[1].strip()))
                    label.setText(textField.getText());
                label.setVisible(true);
                textField.setVisible(false);
            }
        });

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label.setVisible(false);
                textField.setVisible(true);
                textField.requestFocus();
            }
        });

        jpanel.add(textField);

        return label;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        return button;
    }
}
