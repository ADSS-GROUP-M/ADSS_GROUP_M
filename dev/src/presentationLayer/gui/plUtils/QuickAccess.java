package presentationLayer.gui.plUtils;

import javafx.util.Pair;
import presentationLayer.gui.plAbstracts.UIElement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuickAccess implements UIElement {
    private final List<Pair<String, List<Link>>> links;
    private final JPanel panel;
    private final JScrollPane scrollPane;

    public QuickAccess(){
        links = new LinkedList<>();
        panel = new JPanel();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.setBorder(new EmptyBorder(0,7,0,0));
    }

    public QuickAccess addCategory(String name, Link ... links){
        this.links.add(new Pair<>(name, Arrays.asList(links)));
        return this;
    }

    public List<Pair<String, List<Link>>> getLinks(){
        return links;
    }

    @Override
    public Component getComponent() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        int i = 0;
        gbc.gridy = 0;
        for (Pair<String, List<Link>> link : links) {
            JLabel comp = new JLabel(link.getKey());
            comp.setFont(comp.getFont().deriveFont(Font.BOLD).deriveFont(20f));
            gbc.insets = new Insets(0, 0, 20, 0);
            gbc.ipadx = 25;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(comp, gbc);
            gbc.gridy = ++i;


            gbc.insets = new Insets(0, 0, 15, 0);
            gbc.ipadx = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            for (Link l : link.getValue()) {
                panel.add(l.getComponent(), gbc);
                gbc.gridy = ++i;
            }
        }
        return scrollPane;
    }

    @Override
    public void componentResized(Dimension newSize) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        scrollPane.setSize((int)(d.width*0.20),(int)(newSize.getHeight()*0.95));
        scrollPane.revalidate();
    }
}
