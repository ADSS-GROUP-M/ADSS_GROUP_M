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
import presentationLayer.gui.transportModule.model.ObservableTransport;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

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
        constraints.anchor = GridBagConstraints.NORTH;
        idsBox = new SearchBox(ids,"Select item list",new Dimension(200,30),this);
        contentPanel.add(idsBox.getComponent(),constraints);


        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        unLoadingList = new PrettyList(new ObservableList<>(),panel);
        contentPanel.add(unLoadingList.getComponent(),constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.SOUTH;
        loadingList = new PrettyList(new ObservableList<>(),panel);
        contentPanel.add(loadingList.getComponent(),constraints);


        idsBox



//        idsBox.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                ObservableTransport t = (ObservableTransport) ((JList)e.getSource()).getSelectedValue();
//                DefaultListModel<Searchable> model = new DefaultListModel<>();
//                t.route
//                        .forEach(
//                                (s)->model.addElement(new SearchableString("[%s] %s"
//                                        .formatted(t.estimatedArrivalTimes.get(s),sitesMap.get(s).toString())))
//                        );
//                unLoadingList.getList().setModel(model);
//            }
//        });


    }


    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight() * 0.6));
        contentPanel.setPreferredSize(new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250));
        loadingList.componentResized(new Dimension(scrollPane.getWidth(), contentPanel.getHeight()/2));
        unLoadingList.componentResized(new Dimension(scrollPane.getWidth(), contentPanel.getHeight() / 2));
        contentPanel.revalidate();
    }


    

    @Override
    public void notify(ObservableModel observable) {

    }
}
