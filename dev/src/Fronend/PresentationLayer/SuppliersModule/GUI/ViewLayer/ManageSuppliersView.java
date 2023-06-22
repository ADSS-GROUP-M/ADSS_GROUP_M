package Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer;

import Backend.BusinessLayer.SuppliersModule.Supplier;
import Backend.ServiceLayer.SuppliersModule.Response;
import Backend.ServiceLayer.SuppliersModule.SupplierService;
import Fronend.PresentationLayer.SuppliersModule.GUI.ControllersLayer.ManageSuppliersController;
import Fronend.PresentationLayer.SuppliersModule.GUI.SuppliersGUI;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ManageSuppliersView extends JFrame {
    private BackgruondPanel jpanel;
    protected JTextField searchSupplier;
    protected JButton searchB;
    protected JButton addSupplierButton;

    public boolean manager;


    public ManageSuppliersView(boolean manager) {
        this.manager = manager;

        //panel
        jpanel = new BackgruondPanel("src/Fronend/PresentationLayer/SuppliersModule/GUI/ViewLayer/resource/ManageSupplier.png");
        jpanel.setBounds(0,0, 850, 550);
        jpanel.setLayout(null);
        searchSupplier = new JTextField();
        searchSupplier.setBounds(100, 100, 650, 40);
        searchSupplier.setText("enter bn number of supplier");
        searchSupplier.setFocusable(false);
        searchSupplier.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchSupplier.setText("");
                searchSupplier.setFocusable(true);
                searchSupplier.requestFocusInWindow();
            }
        });
        searchSupplier.addActionListener((ActionEvent e)->searchSupplier.setText(""));
        searchSupplier.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    searchB.doClick();
                if(e.getKeyCode() == 47) {
                }
            }
            @Override
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == 47) {
                    try {
                        String text = searchSupplier.getText().substring(0, searchSupplier.getText().length() - 1);
                        searchSupplier.setText(text);
                    }
                    catch (Exception ignored){}
                }
            }
        });
        //search button
        searchB = new JButton("search");
        searchB.setBounds(350, 150, 100, 30);
        searchB.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    searchB.doClick();
                if(e.getKeyCode() == 47) {
                    // Create a MouseEvent
                    int x = 110; // X-coordinate of the event
                    int y = 110; // Y-coordinate of the event
                    int clickCount = 1; // Number of clicks
                    int modifiers = MouseEvent.BUTTON1_DOWN_MASK; // Mouse button modifiers
                    long eventTime = System.currentTimeMillis(); // Time of the event
                    MouseEvent event = new MouseEvent(searchSupplier, MouseEvent.MOUSE_CLICKED, eventTime, modifiers, x, y, clickCount, false);
                    searchSupplier.dispatchEvent(event);
                }
            }
        });
        //add supplier button
        addSupplierButton = new JButton("Add Supplier");
        addSupplierButton.setBounds(100, 450, 120, 30);
        addSupplierButton.addActionListener((ActionEvent e) -> addSupplier());


        JButton back = new JButton("Back");
        back.setBounds(20, 450, 70, 30);
        back.addActionListener((ActionEvent e)->{
            dispose();
            new SuppliersGUI(manager);
        });
        back.setFont(new Font("Comic Sans", Font.BOLD, 12));
        back.setFocusable(false);
        jpanel.add(back);


        jpanel.add(addSupplierButton);
        jpanel.add(searchSupplier);
        jpanel.add(searchB);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(330, 150, 865, 550);
        this.setVisible(true);
        this.add(jpanel);
    }


    public void registerToSearchB(ActionListener actionListener){
        searchB.addActionListener(actionListener);
    }

    public JButton getSearchB() {
        return searchB;
    }

    public String getSearchText(){
        return searchSupplier.getText();
    }

    public void riseNotFoundSupplier(String bnNumber){
        JOptionPane.showMessageDialog(null, "no such supplier: " + bnNumber, "invalid supplier", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showSupplier(Supplier supplier){
        jpanel.removeAll();
//        jpanel.revalidate();
//        jpanel.repaint();

    }

    private void addSupplier(){
        new AddSupplierGUI().setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println(KeyEvent.VK_ENTER);
    }
}
