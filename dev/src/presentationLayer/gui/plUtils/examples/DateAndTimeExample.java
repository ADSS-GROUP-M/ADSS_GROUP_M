package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import java.text.SimpleDateFormat;

public class DateAndTimeExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("DateTime Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        // Create a date field
        JLabel dateLabel = new JLabel("Date: ");
        panel.add(dateLabel);

        JFormattedTextField dateField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        dateField.setColumns(10);
        panel.add(dateField);

        // Create a time field
        JLabel timeLabel = new JLabel("Time: ");
        panel.add(timeLabel);

        JFormattedTextField timeField = new JFormattedTextField(new SimpleDateFormat("HH:mm:ss"));
        timeField.setColumns(8);
        panel.add(timeField);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
