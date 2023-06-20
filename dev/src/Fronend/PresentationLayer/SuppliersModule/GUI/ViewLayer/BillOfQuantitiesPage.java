package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.SuppliersModule.BillOfQuantities;
import Backend.BusinessLayer.SuppliersModule.Discounts.*;
import Backend.ServiceLayer.SuppliersModule.BillOfQuantitiesService;
import Backend.ServiceLayer.SuppliersModule.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;

public class BillOfQuantitiesPage extends JPanel {
    private Gson gson = new Gson();
    private String bnNumber;
    private BillOfQuantitiesService billOfQuantitiesService = new BillOfQuantitiesService();
    private BillOfQuantities billOfQuantities;

    private JButton editAmountToReachButton;
    private JButton editDiscountOnAmountButton;
    private JButton editPriceToReachButton;
    private JButton editDiscountOnTotalButton;
    private JButton editAmountBeforeTotalButton;
    private JButton showProductsDiscountButton;

    public BillOfQuantitiesPage(String bnNumber) {
        Type responseOfBOQ = new TypeToken<Response<BillOfQuantities>>(){}.getType();
        billOfQuantities =((Response<BillOfQuantities>) gson.fromJson(billOfQuantitiesService.getBillOfQuantities(bnNumber), responseOfBOQ)).getReturnValue();
        this.bnNumber = bnNumber;
        billOfQuantitiesService = new BillOfQuantitiesService();
        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        editAmountToReachButton = new JButton("Edit");
        editAmountToReachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAmountToReach();
            }
        });
        add(createLabelWithButton("Amount to reach for discount:", String.valueOf(billOfQuantities.getAmountToReachForDiscount()),
                editAmountToReachButton));

        editDiscountOnAmountButton = new JButton("Edit");
        editDiscountOnAmountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDiscountOnAmount();
            }
        });

        add(createLabelWithButton("Discount on amount:", billOfQuantities.getDiscountOnAmount() != null ? billOfQuantities.getDiscountOnAmount().toString() : "0",
                editDiscountOnAmountButton));

        editPriceToReachButton = new JButton("Edit");
        editPriceToReachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editPriceToReach();
            }
        });
        add(createLabelWithButton("Price to reach for discount:", String.valueOf(billOfQuantities.getPriceToReachForDiscount()),
                editPriceToReachButton));

        editDiscountOnTotalButton = new JButton("Edit");
        editDiscountOnTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDiscountOnTotal();
            }
        });
        add(createLabelWithButton("Discount on total:", billOfQuantities.getDiscountOnTotal() != null ? billOfQuantities.getDiscountOnTotal().toString() : "0",
                editDiscountOnTotalButton));

        editAmountBeforeTotalButton = new JButton("Edit");
        editAmountBeforeTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAmountBeforeTotal();
            }
        });
        add(createLabelWithButton("Amount before total:", String.valueOf(billOfQuantities.getAmountBeforeTotal()),
                editAmountBeforeTotalButton));

        showProductsDiscountButton = new JButton("show products discounts");
        showProductsDiscountButton.addActionListener((ActionEvent e) -> showProductsDiscounts());
        showProductsDiscountButton.setFont(new Font(null, Font.PLAIN, 15));
        add(showProductsDiscountButton);
    }

    private JPanel createLabelWithButton(String labelText, String description, JButton editButton) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel label = new JLabel(labelText);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        label.setForeground(Color.DARK_GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(descriptionLabel.getFont().deriveFont(Font.PLAIN, 16f));
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);

        panel.add(label, BorderLayout.WEST);
        panel.add(descriptionLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private void editAmountToReach() {
        String inputValue = JOptionPane.showInputDialog(this, "Enter the new amount to reach for discount:");
        try {
            int newAmountToReach = Integer.parseInt(inputValue);
            Response r;
            if ((r = gson.fromJson(billOfQuantitiesService.setDiscountOnAmountOfProducts(bnNumber, newAmountToReach, billOfQuantities.getDiscountOnAmount()), Response.class)).isSuccess())
                billOfQuantities.setDiscountOnAmountOfProducts(newAmountToReach, billOfQuantities.getDiscountOnAmount());
            else
                throw new Exception(r.getError());
            JOptionPane.showMessageDialog(this, "Amount to reach for discount updated successfully!");
            updateLabels();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid integer.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editDiscountOnAmount() {
        String[] discountOptions = { "Cash Discount", "Percentage Discount" };
        String selectedOption = (String) JOptionPane.showInputDialog(this, "Choose the discount type:", "Edit Discount on Amount",
                JOptionPane.PLAIN_MESSAGE, null, discountOptions, discountOptions[0]);
        if (selectedOption != null) {
            Response r;
            if (selectedOption.equals("Cash Discount")) {
                String inputValue = JOptionPane.showInputDialog(this, "Enter the new discount on amount:");
                try {
                    double newDiscountOnAmount = Double.parseDouble(inputValue);
                    if((r = gson.fromJson(billOfQuantitiesService.setDiscountOnAmountOfProducts(bnNumber, billOfQuantities.getAmountToReachForDiscount(), new CashDiscount(newDiscountOnAmount)), Response.class)).isSuccess())
                        billOfQuantities.setDiscountOnAmountOfProducts(billOfQuantities.getAmountToReachForDiscount(), new CashDiscount(newDiscountOnAmount));
                    else
                        throw new Exception(r.getError());
                    JOptionPane.showMessageDialog(this, "Discount on amount updated successfully!");
                    updateLabels();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid double.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (selectedOption.equals("Percentage Discount")) {
                String inputValue = JOptionPane.showInputDialog(this, "Enter the new percentage discount on amount:");
                try {
                    double newDiscountOnAmount = Double.parseDouble(inputValue);
                    if((r = gson.fromJson(billOfQuantitiesService.setDiscountOnAmountOfProducts(bnNumber, billOfQuantities.getAmountToReachForDiscount(), new PercentageDiscount(newDiscountOnAmount)), Response.class)).isSuccess())
                        billOfQuantities.setDiscountOnAmountOfProducts(billOfQuantities.getAmountToReachForDiscount(),new PercentageDiscount(newDiscountOnAmount));
                    else
                        throw new Exception(r.getError());
                    JOptionPane.showMessageDialog(this, "Discount on amount updated successfully!");
                    updateLabels();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid double.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void editPriceToReach() {
        String inputValue = JOptionPane.showInputDialog(this, "Enter the new price to reach for discount:");
        try {
            double newPriceToReach = Double.parseDouble(inputValue);
            Response r;
            if((r = gson.fromJson(billOfQuantitiesService.setDiscountOnTotalOrder(bnNumber, newPriceToReach, billOfQuantities.getDiscountOnTotal()), Response.class)).isSuccess())
                billOfQuantities.setDiscountOnTotalOrder(newPriceToReach, billOfQuantities.getDiscountOnTotal());
            else
                throw new Exception(r.getError());
            JOptionPane.showMessageDialog(this, "Price to reach for discount updated successfully!");
            updateLabels();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid double.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editDiscountOnTotal() {
        String[] discountOptions = { "Cash Discount", "Percentage Discount" };
        String selectedOption = (String) JOptionPane.showInputDialog(this, "Choose the discount type:", "Edit Discount on Total",
                JOptionPane.PLAIN_MESSAGE, null, discountOptions, discountOptions[0]);
        if (selectedOption != null) {
            Response r;
            if (selectedOption.equals("Cash Discount")) {
                String inputValue = JOptionPane.showInputDialog(this, "Enter the new discount on total:");
                try {
                    double newDiscountOnTotal = Double.parseDouble(inputValue);
                    if((r = gson.fromJson(billOfQuantitiesService.setDiscountOnTotalOrder(bnNumber, billOfQuantities.getPriceToReachForDiscount(), new CashDiscount(newDiscountOnTotal)), Response.class)).isSuccess())
                        billOfQuantities.setDiscountOnTotalOrder(billOfQuantities.getPriceToReachForDiscount(), new CashDiscount(newDiscountOnTotal));
                    else
                        throw new Exception(r.getError());
                    JOptionPane.showMessageDialog(this, "Discount on total updated successfully!");
                    updateLabels();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid double.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (selectedOption.equals("Percentage Discount")) {
                String inputValue = JOptionPane.showInputDialog(this, "Enter the new percentage discount on total:");
                try {
                    double newDiscountOnTotal = Double.parseDouble(inputValue);
                    if((r = gson.fromJson(billOfQuantitiesService.setDiscountOnTotalOrder(bnNumber, billOfQuantities.getPriceToReachForDiscount(), new PercentageDiscount(newDiscountOnTotal)), Response.class)).isSuccess())
                        billOfQuantities.setDiscountOnTotalOrder(billOfQuantities.getPriceToReachForDiscount(),new PercentageDiscount(newDiscountOnTotal));
                    else throw new Exception(r.getError());
                    JOptionPane.showMessageDialog(this, "Discount on total updated successfully!");
                    updateLabels();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid double.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void editAmountBeforeTotal() {
        String[] options = { "true", "false" };
        String selectedOption = (String) JOptionPane.showInputDialog(this, "Choose the new amount before total:", "Edit Amount Before Total",
                JOptionPane.PLAIN_MESSAGE, null, options, String.valueOf(billOfQuantities.getAmountBeforeTotal()));
        if (selectedOption != null) {
            try {
                boolean newAmountBeforeTotal = Boolean.parseBoolean(selectedOption);
                billOfQuantitiesService.setOrderOfDiscounts(bnNumber, newAmountBeforeTotal);
                billOfQuantities.setOrderOfDiscounts(newAmountBeforeTotal);
                JOptionPane.showMessageDialog(this, "Amount before total updated successfully!");
                updateLabels();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter 'true' or 'false'.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showProductsDiscounts(){
        new DiscountTable(bnNumber).setVisible(true);
    }



    private void updateLabels() {
        removeAll();
        add(createLabelWithButton("Amount to reach for discount:", String.valueOf(billOfQuantities.getAmountToReachForDiscount()),
                editAmountToReachButton));
        add(createLabelWithButton("Discount on amount:", billOfQuantities.getDiscountOnAmount() != null ? billOfQuantities.getDiscountOnAmount().toString() : "0",
                editDiscountOnAmountButton));
        add(createLabelWithButton("Price to reach for discount:", String.valueOf(billOfQuantities.getPriceToReachForDiscount()),
                editPriceToReachButton));
        add(createLabelWithButton("Discount on total:", billOfQuantities.getDiscountOnTotal() != null ? billOfQuantities.getDiscountOnTotal().toString() : "0",
                editDiscountOnTotalButton));
        add(createLabelWithButton("Amount before total:", String.valueOf(billOfQuantities.getAmountBeforeTotal()),
                editAmountBeforeTotalButton));
        showProductsDiscountButton = new JButton("show products discounts");
        showProductsDiscountButton.addActionListener((ActionEvent e) -> showProductsDiscounts());
        showProductsDiscountButton.setFont(new Font(null, Font.PLAIN, 15));
        add(showProductsDiscountButton);
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(650, 400);
    }


}
