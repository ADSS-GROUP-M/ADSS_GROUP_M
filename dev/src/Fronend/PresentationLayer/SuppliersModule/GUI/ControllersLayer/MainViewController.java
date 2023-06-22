package Fronend.PresentationLayer.SuppliersModule.GUI.ControllersLayer;

import Fronend.PresentationLayer.SuppliersModule.GUI.ModelLayer.SuppliersModel;
import Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer.MainView;
import Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer.ManageSuppliersView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainViewController implements ActionListener {
    private MainView mainView;

    public MainViewController(MainView mainView) {
        this.mainView = mainView;
        mainView.registerToSupplierInfo(this);
        mainView.registerToMangeOrders(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == mainView.getSuppliersInfoB()){
            //go to supplier info page
            new ManageSuppliersController(new ManageSuppliersView(mainView.manager), new SuppliersModel());
            mainView.dispose();
        }
        else{
            //go to manage order page
        }
    }
}
