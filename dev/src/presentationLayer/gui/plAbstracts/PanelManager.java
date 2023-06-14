package presentationLayer.gui.plAbstracts;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class PanelManager implements UIElement {

    private final CardLayout cardLayout;
    private final JPanel panel;
    private Panel currentPanel;
    private final LinkedList<Panel> panelsHistory = new LinkedList<>();

    public PanelManager(Panel startingPanel) {
        cardLayout = new CardLayout();
        panel = new JPanel();
        panel.setLayout(cardLayout);
        panel.setLocation((int)(Window.screenSize.width*0.2), 0);
        String name = startingPanel.getClass().getSimpleName();
        panel.add(startingPanel.getComponent(),name);
        cardLayout.show(panel,name);
        currentPanel = startingPanel;
    }

    public void setPanel(Panel panel) {

        if(currentPanel.getClass().getSimpleName().equals(panel.getClass().getSimpleName())) {
            return;
        }

        panelsHistory.push(currentPanel);
        currentPanel = panel;
        this.panel.removeAll();
        String name = panel.getClass().getSimpleName();
        this.panel.add(panel.getComponent(),name);
        cardLayout.show(this.panel, name);
        this.panel.validate();
        this.panel.repaint();
    }

    public void previousPanel(){
        if(panelsHistory.size() > 0){
            setPanel(panelsHistory.pop());
        }
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void componentResized(Dimension newSize) {
        panel.setSize(new Dimension((int)(newSize.width*0.8),newSize.height));
        panel.validate();
    }
}
