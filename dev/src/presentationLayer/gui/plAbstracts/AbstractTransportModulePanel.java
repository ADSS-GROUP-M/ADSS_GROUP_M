package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.ObjectObserver;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.ContentPanel;

import java.awt.*;

public abstract class AbstractTransportModulePanel extends ScrollablePanel implements ObjectObserver {

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
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension preferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight()*0.6));
        contentPanel.setPreferredSize(preferredSize);
        scrollPane.revalidate();
    }
}
