package Fronend.PresentationLayer;

import Fronend.PresentationLayer.InventoryModule.gui.InventoryMainMenu;
import Fronend.PresentationLayer.SuppliersModule.GUI.SuppliersGUI;
import Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer.BackgruondPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ManagerWindowGUI extends JFrame {

    private BackgruondPanel jpanel;

    public ManagerWindowGUI(){
        //panel
        jpanel = new BackgruondPanel("src/Fronend/PresentationLayer/SuppliersModule/GUI/ViewLayer/resource/supermarketMain.png");
        jpanel.setBounds(0,0, 850, 550);
        jpanel.setLayout(null);
        setResizable(false);

        JLabel welcomeLabel = new JLabel("WELCOME TO SUPER-LI MANAGEMENT SYSTEM");
        welcomeLabel.setBounds(55, 50, 850, 200);
        welcomeLabel.setFont(new Font("Comic Sans", Font.BOLD, 30));
        welcomeLabel.setForeground(Color.black);


        JButton suppliersGUIButton = new JButton("Manage Suppliers");
        suppliersGUIButton.setBounds(120, 350, 200, 50);
        suppliersGUIButton.setFont(new Font("Comic Sans", Font.BOLD, 15));
        suppliersGUIButton.setFocusable(false);
        suppliersGUIButton.addActionListener((ActionEvent e)->{
            dispose();
            new SuppliersGUI(true);
        });
        JButton inventoryGUIButton = new JButton("Manage Inventory");
        inventoryGUIButton.setBounds(510, 350, 200, 50);
        inventoryGUIButton.setFont(new Font("Comic Sans", Font.BOLD, 15));
        inventoryGUIButton.setFocusable(false);
        inventoryGUIButton.addActionListener((ActionEvent e)->{
            dispose();
            InventoryMainMenu.main(new String[0]);
        });
        jpanel.add(suppliersGUIButton);
        jpanel.add(inventoryGUIButton);
        jpanel.add(welcomeLabel);
        add(jpanel);
        this.setBounds(330, 150, 850, 550);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        new ManagerWindowGUI();
    }

}
