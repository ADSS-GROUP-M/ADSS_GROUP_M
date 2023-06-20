package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.SuppliersModule.Order;
import Backend.ServiceLayer.SuppliersModule.OrderService;
import Backend.ServiceLayer.SuppliersModule.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Type;
import java.util.List;

public class SupplierOrdersGUI extends JFrame {
    private OrderService orderService = new OrderService();
    private Gson gson = new Gson();
    private String bnNumber;

    public SupplierOrdersGUI(String bnNumber){
        this.bnNumber = bnNumber;
        setTitle("supplier order history");
        setLayout(new BorderLayout());
        //getting the list of orders
        java.lang.reflect.Type responseOfListOfOrders = new TypeToken<Response<List<Order>>>(){}.getType();
        List<Order> orderHistory = ((Response<List<Order>>)gson.fromJson(orderService.getOrderHistory(bnNumber), responseOfListOfOrders)).getReturnValue();
        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        if(orderHistory == null){
            mainPanel = new JPanel(null);
            setContentPane(mainPanel);
            setResizable(false);
            JLabel noHistory = new JLabel("The selected supplier have no order history");
            noHistory.setBounds(140, 150, 400, 30);
            noHistory.setFont(new Font(null, Font.BOLD, 15));
            mainPanel.add(noHistory);
        }
        else {
            // Create the table model with columns for "Catalog Number" and "Quantity"
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Order No."}, 0);
            for (int i = 0; i < orderHistory.size(); i++) {
                tableModel.addRow(new Object[]{i});
            }

            // Create the JTable with the populated table model
            JTable orderTable = new JTable(tableModel);
            // Create a scroll pane and add the table to it
            JScrollPane scrollPane = new JScrollPane(orderTable);

            // Add the scroll pane to the main panel
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            JButton showSelectedOrderButton = new JButton("Show Selected Order");
            showSelectedOrderButton.addActionListener((ActionEvent e) -> {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    new OrderGUI(orderHistory.get(selectedRow).getProducts());
                }
            });
            mainPanel.add(showSelectedOrderButton, BorderLayout.SOUTH);
        }
        this.setBounds(330, 150, 600, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SupplierOrdersGUI("1");
    }
}
