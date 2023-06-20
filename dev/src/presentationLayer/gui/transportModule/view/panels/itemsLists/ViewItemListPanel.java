package presentationLayer.gui.transportModule.view.panels.itemsLists;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyList;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;
import presentationLayer.gui.transportModule.control.ItemListsControl;
import presentationLayer.gui.transportModule.model.ObservableItemList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewItemListPanel extends AbstractTransportModulePanel {
    private SearchBox idsBox;
    private PrettyList unLoadingList;
    private PrettyList loadingList;

    public ViewItemListPanel(ItemListsControl control) {
        super(control);
        init();
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void init() {

        ObservableList emptyList = new ObservableList<>();
        control.getAll(this,emptyList);
        ObservableList<ObservableItemList> itemLists = emptyList;

        final ObservableList<Searchable> ids = new ObservableList<>();
        itemLists.forEach(ils -> ids.add(new SearchableString(String.valueOf(ils.id))));

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        constraints.gridy = 0;
        idsBox = new SearchBox(ids,"Select item list",new Dimension(200,30),panel);
        contentPanel.add(idsBox.getComponent(),constraints);


        constraints.gridy = 1;
        unLoadingList = new PrettyList(new ObservableList<>(),panel);
        contentPanel.add(unLoadingList.getComponent(),constraints);

        constraints.gridy = 2;
        loadingList = new PrettyList(new ObservableList<>(),panel);
        contentPanel.add(loadingList.getComponent(),constraints);

        idsBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                Object o = idsBox.getSelected();
                if(o == null || o instanceof String s && (s.equals("") || s.trim().equals("Select item list") )){
                    unLoadingList.getList().setModel(new DefaultListModel<>());
                    loadingList.getList().setModel(new DefaultListModel<>());
                    return;
                }

                int index;
                try{
                    index = Integer.parseInt(o.toString())-1;
                } catch(NumberFormatException ex){
                    return;
                }
                ObservableItemList ils = itemLists.get(index);

                DefaultListModel<Searchable> unLoadModel = new DefaultListModel<>();
                ils.unload.forEach((key, value) -> unLoadModel.addElement(new SearchableString("%s %s".formatted(value, key))));

                DefaultListModel<Searchable> loadModel = new DefaultListModel<>();
                ils.load.forEach((key, value) -> loadModel.addElement(new SearchableString("%s %s".formatted(value, key))));

                unLoadingList.getList().setModel(unLoadModel);
                loadingList.getList().setModel(loadModel);
            }
        });
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        loadingList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (-50 + contentPanelSize.height/2.0)));
        unLoadingList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (-50 + contentPanelSize.height/2.0)));

        panel.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
