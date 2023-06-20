package Fronend.PresentationLayer.SuppliersModule.GUI;

import Fronend.PresentationLayer.SuppliersModule.GUI.ControllersLayer.MainViewController;
import Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer.MainView;

public class SuppliersGUI {
    public SuppliersGUI(boolean manager){
        MainView mainView = new MainView(manager);
        MainViewController mainViewController = new MainViewController(mainView);
    }

}
