package presentationLayer.gui.transportModule.view.panels.itemsLists;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.transportModule.control.ItemListsControl;

import java.awt.*;

public class UpdateItemListPanel extends AbstractTransportModulePanel {


    public UpdateItemListPanel(ItemListsControl control) {
        super(control);
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        panel.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
