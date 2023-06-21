package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractTransportModulePanel extends ScrollablePanel implements ModelObserver {

    protected ContentPanel contentPanel;
    protected AbstractControl control;

    protected AbstractTransportModulePanel(AbstractControl control) {
        super();
        this.control = control;
        subscribe(control);
        init();
    }

    private void init() {
        contentPanel = new ContentPanel(Colors.getContentPanelColor());
        contentPanel.setLayout(new GridBagLayout());
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.add(contentPanel,gbc);
        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                contentPanel.requestFocus();
                SwingUtilities.invokeLater(panel::repaint);
            }
        });
    }

    protected abstract void clearFields();

    @Override
    public void notify(ObservableModel observable) {
        if (observable.errorOccurred()) {
            JOptionPane.showMessageDialog(this.getComponent(), observable.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getComponent(), observable.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        }
    }
}
