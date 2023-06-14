package presentationLayer.gui.employeeModule.view;

import presentationLayer.gui.employeeModule.view.panels.EmployeesPanel;
import presentationLayer.gui.plAbstracts.Window;

public class HRManagerView extends Window {
    public HRManagerView() {
        super("HRManager Page");

        super.addComponent(new EmployeesPanel());
        super.init();
        super.setVisible(true);
//        super.addComponent(initQuickAccess());
    }

//    private QuickAccess initQuickAccess(){
//        return new QuickAccess()
//                .addCategory("Transport Management",
//                        new Link("View Transports", () -> super.setCurrentPanel(new ViewTransportsPanel())),
//                        new Link("Add Transport", () -> super.setCurrentPanel(new AddTransportPanel())),
//                        new Link("Update Transport", () -> super.setCurrentPanel(new UpdateTransportPanel())),
//                        new Link("Remove Transport", () -> super.setCurrentPanel(new RemoveTransportPanel()))
//                );
//    }
    public static void main(String[] args) {
        Window window = new HRManagerView();
    }
}