package Fronend.PresentationLayer.SuppliersModule.GUI;

import Fronend.PresentationLayer.SuppliersModule.GUI.ControllersLayer.MainViewController;
import Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer.MainView;

public class GUI {
    public GUI(){
        MainView mainView = new MainView();
        MainViewController mainViewController = new MainViewController(mainView);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
