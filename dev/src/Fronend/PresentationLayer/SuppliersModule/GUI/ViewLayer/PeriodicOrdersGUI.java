package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.SuppliersModule.Order;
import Backend.BusinessLayer.SuppliersModule.PeriodicOrder;
import Backend.ServiceLayer.SuppliersModule.PeriodicOrderService;
import Backend.ServiceLayer.SuppliersModule.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class PeriodicOrdersGUI extends JFrame {
    private PeriodicOrderService periodicOrderService = new PeriodicOrderService();
    private Gson gson = new Gson();
    private JTable orderTable;
    private Map<Integer, PeriodicOrder> periodicOrders;

    public PeriodicOrdersGUI() {
        setTitle("Periodic Orders");
        setLayout(new BorderLayout());
        //getting the list of orders
        java.lang.reflect.Type responseOfPeriodicOrders = new TypeToken<Response<Map<Integer, PeriodicOrder>>>(){}.getType();
        periodicOrders = ((Response<Map<Integer, PeriodicOrder>>)gson.fromJson(periodicOrderService.getAllPeriodicOrders(), responseOfPeriodicOrders)).getReturnValue();
        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        if(periodicOrders == null || periodicOrders.isEmpty()){
            mainPanel = new JPanel(null);
            setContentPane(mainPanel);
            setResizable(false);
            JLabel noHistory = new JLabel("There are no periodic orders");
            noHistory.setBounds(140, 150, 400, 30);
            noHistory.setFont(new Font(null, Font.BOLD, 15));
            mainPanel.add(noHistory);
        }
        else {
            JPanel secondaryPanel = new JPanel(new BorderLayout());
            // Create the table model with columns for "Catalog Number" and "Quantity"
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Bn Number", "Order No.", "Day", "Branch"}, 0);
            for(Map.Entry<Integer, PeriodicOrder> periodicOrder : periodicOrders.entrySet()){
                int orderId = periodicOrder.getKey();
                String bnNumber = periodicOrder.getValue().getBnNumber();
                int day = periodicOrder.getValue().getDay();
                String branch = periodicOrder.getValue().getBranch().name();
                tableModel.addRow(new Object[]{bnNumber, orderId, day, branch});
            }

            // Create the JTable with the populated table model
            orderTable = new JTable(tableModel);
            // Create a scroll pane and add the table to it
            JScrollPane scrollPane = new JScrollPane(orderTable);

            // Add the scroll pane to the main panel
            secondaryPanel.add(scrollPane, BorderLayout.CENTER);

            JButton showSelectedOrderButton = new JButton("Show Selected Order");
            showSelectedOrderButton.addActionListener((ActionEvent e) -> {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    new OrderGUI(periodicOrders.get(orderTable.getValueAt(selectedRow, 1)).getOrder().getProducts());
                }
            });
            JButton addPeriodicOrder = new JButton("Add Periodic Order");
            addPeriodicOrder.addActionListener((ActionEvent e)->{
                addPeriodicOrder();
            });
            secondaryPanel.add(addPeriodicOrder, BorderLayout.SOUTH);
            mainPanel.add(secondaryPanel, BorderLayout.CENTER);
            mainPanel.add(showSelectedOrderButton, BorderLayout.SOUTH);

            JButton removeRowButton = new JButton("Remove Selected Row");
            removeRowButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeSelectedRow();
                }
            });
            add(removeRowButton, BorderLayout.SOUTH);
        }
        this.setBounds(330, 150, 600, 400);
        setVisible(true);
    }

    private void removeSelectedRow() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderId = Integer.parseInt(orderTable.getValueAt(selectedRow, 1).toString());
            Response r = gson.fromJson(periodicOrderService.removePeriodicOrder(orderId), Response.class);
            if(r.errorOccurred())
                JOptionPane.showMessageDialog(null, r.getError(), "error", JOptionPane.ERROR_MESSAGE);
            else {
                JOptionPane.showMessageDialog(null, r.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
                periodicOrders.remove(orderId);
                // remove the row
                DefaultTableModel tableModel = (DefaultTableModel) orderTable.getModel();
                tableModel.removeRow(selectedRow);
            }
        }
    }

    /**
     * return the order id of a new periodic order
     * @return the max value of order id in the table + 1
     */
    private int getOrderId(){
        int orderId = -1;
        for(int i = 0; i < orderTable.getRowCount(); i++){
            orderId = Math.max(orderId, Integer.parseInt(orderTable.getValueAt(i, 1).toString()));
        }
        return orderId + 1;
    }
    private void addPeriodicOrder() {
        // Prompt the user for input
        String bnNumber = JOptionPane.showInputDialog(this, "Enter Bn Number:");
        int orderId = getOrderId();
        boolean legal = true;
        int day = -1;
        try {
            day = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Day (0-sunday, 6-saturday):"));
            if(day < 0 || day > 6)
                throw new Exception("invalid day");
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, "Invalid day", "error", JOptionPane.ERROR_MESSAGE);
            legal = false;
        }
        if(legal) {
            Branch branch = null;
            try {
                String[] branchOptions = {"branch1", "branch2", "branch3", "branch4", "branch5", "branch6", "branch7", "branch8", "branch9"};
                String selectedBranch = (String) JOptionPane.showInputDialog(this, "Select Branch:", "Branch Selection", JOptionPane.PLAIN_MESSAGE, null, branchOptions, branchOptions[0]);
                branch = Branch.valueOf(selectedBranch);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid Branch", "error", JOptionPane.ERROR_MESSAGE);
                legal = false;
            }
            if(legal) {
                boolean cond = true;
                Order order = new Order();
                while (cond) {
                    String catalogNumber;
                    int quantity = -1;
                    try {
                        catalogNumber = JOptionPane.showInputDialog(this, "Enter Catalog Number:", "Add Product", JOptionPane.PLAIN_MESSAGE);
                        cond = catalogNumber != null && !catalogNumber.equals("" + JOptionPane.CANCEL_OPTION);
                        if (cond) {
                            quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Quantity:", "Add Product", JOptionPane.PLAIN_MESSAGE));
                            cond = quantity != JOptionPane.CANCEL_OPTION;
                            if (cond)
                                order.addProduct(catalogNumber, quantity);
                        }
                    } catch (Exception e) {
                        cond = false;
                    }
                }
                if (order.getProducts().isEmpty())
                    JOptionPane.showMessageDialog(null, "No products selected", "error", JOptionPane.ERROR_MESSAGE);
                else {
                    Response r = gson.fromJson(periodicOrderService.addPeriodicOrder(bnNumber, order.getProducts(), day, branch), Response.class);
                    if (r.errorOccurred())
                        JOptionPane.showMessageDialog(null, r.getError(), "error", JOptionPane.ERROR_MESSAGE);
                    else {
                        JOptionPane.showMessageDialog(null, r.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
                        periodicOrders.put(orderId, new PeriodicOrder(bnNumber, order, day, branch));
                        // Add the new order to the table model
                        ((DefaultTableModel) orderTable.getModel()).addRow(new Object[]{bnNumber, orderId, day, branch});
                    }
                }
            }
        }
    }
}
