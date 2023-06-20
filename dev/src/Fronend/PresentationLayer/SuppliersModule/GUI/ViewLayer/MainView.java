package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private BackgruondPanel jpanel;
    private JButton suppliersInfoB;
    private JButton manageOrdersB;
    private JLabel welcomeLabel;
    public MainView(){

        //panel
        jpanel = new BackgruondPanel("src/Fronend/PresentationLayer/SuppliersModule/GUI/ViewLayer/resource/supermarketMain.png");
        jpanel.setBounds(0,0, 850, 550);
        jpanel.setLayout(null);
        //buttons - suppliers info
        suppliersInfoB = new JButton("manage suppliers");
        suppliersInfoB.setBounds(120, 350, 200, 50);
        suppliersInfoB.setFont(new Font("Comic Sans", Font.BOLD, 15));
        suppliersInfoB.setFocusable(false);
        //buttons - manage orders
        manageOrdersB = new JButton("manage orders");
        manageOrdersB.setBounds(510, 350, 200, 50);
        manageOrdersB.setFont(new Font("Comic Sans", Font.BOLD, 15));
        manageOrdersB.addActionListener((ActionEvent e)->{
            new PeriodicOrdersGUI();
        });
        manageOrdersB.setFocusable(false);
        //welcome label
        welcomeLabel = new JLabel("WELCOME TO SUPER-LI SUPPLIERS MANAGEMENT");
        //welcomeLabel.setIcon(new ImageIcon(new ImageIcon("src/Fronend/PresentationLayer/SuppliersModule/GUI/ViewLayer/resource/shoppingCart.png").getImage().getScaledInstance(50, 50,java.awt.Image.SCALE_SMOOTH)));
        welcomeLabel.setBounds(25, 80, 850, 200);
        welcomeLabel.setFont(new Font("Comic Sans", Font.BOLD, 30));
        welcomeLabel.setForeground(Color.black);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(330, 150, 850, 550);
        this.setVisible(true);
        setResizable(false);
        this.add(jpanel);
        jpanel.add(suppliersInfoB);
        jpanel.add(manageOrdersB);
        jpanel.add(welcomeLabel);
    }

    public JButton getSuppliersInfoB() {
        return suppliersInfoB;
    }

    public JButton getManageOrdersB() {
        return manageOrdersB;
    }

    public void registerToSupplierInfo(ActionListener actionListener){
        suppliersInfoB.addActionListener(actionListener);
    }
    public void registerToMangeOrders(ActionListener actionListener){
        manageOrdersB.addActionListener(actionListener);
    }

    public static void main(String[] args) {
        new MainView();
    }
}
