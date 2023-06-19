package Fronend.PresentationLayer.SuppliersModule.GUI.ControllersLayer;

import Backend.BusinessLayer.SuppliersModule.Supplier;
import Fronend.PresentationLayer.SuppliersModule.GUI.ModelLayer.SuppliersModel;
import Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer.ManageSuppliersView;
import Fronend.PresentationLayer.SuppliersModule.GUI.ViewLayer.SupplierPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageSuppliersController implements ActionListener {
    private ManageSuppliersView manageSuppliersView;
    private SuppliersModel suppliersModel;
    public ManageSuppliersController(ManageSuppliersView manageSuppliersView, SuppliersModel suppliersModel) {
        this.manageSuppliersView = manageSuppliersView;
        this.suppliersModel = suppliersModel;
        manageSuppliersView.registerToSearchB(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == manageSuppliersView.getSearchB()){
            String bnNumber = manageSuppliersView.getSearchText();
            Supplier s = suppliersModel.getSupplier(bnNumber);
            if(s == null)
                manageSuppliersView.riseNotFoundSupplier(bnNumber);
            else {
                //manageSuppliersView.showSupplier(s);
                manageSuppliersView.dispose();
                SupplierPage sp = new SupplierPage();
                sp.initPage(s, this);
            }
        }
    }

    public boolean setName(String bnNumber, String name){
        return suppliersModel.setName(bnNumber, name);
    }

    public Boolean setBnNumber(String bnNumber, String value) {
        return suppliersModel.setBnNumber(bnNumber, value);
    }

    public Boolean setBankAccount(String bnNumber, String value) {
        return suppliersModel.setBankAccount(bnNumber, value);
    }


    public Boolean setPaymentMethod(String bnNumber, String value) {
        return suppliersModel.setPaymentMethod(bnNumber, value);

    }
}
