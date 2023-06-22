package Fronend.PresentationLayer.SuppliersModule.GUI;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class CustomButtonOptionPaneExample {
    public static void main(String[] args) {
        // Create an array of button text
        String[] buttonText = { "Yes, please!", "No, thanks!", "Cancel" };

        // Set the custom button text
        UIManager.put("OptionPane.yesButtonText", buttonText[0]);
        UIManager.put("OptionPane.noButtonText", buttonText[1]);
        UIManager.put("OptionPane.cancelButtonText", buttonText[2]);


        // Show the option pane with custom button text
        int choice = JOptionPane.showConfirmDialog(null, "Do you want to proceed?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);

        // Get the selected button index
        if (choice == JOptionPane.YES_OPTION) {
            System.out.println("User chose: " + buttonText[0]);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.out.println("User chose: " + buttonText[1]);
        } else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
            System.out.println("User chose: " + buttonText[2]);
        }
    }
}
