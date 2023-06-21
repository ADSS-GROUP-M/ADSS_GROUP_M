package presentationLayer.gui.employeeModule.view;

import presentationLayer.PresentationFactory;
import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.controller.UsersControl;
import presentationLayer.gui.employeeModule.view.panels.EmployeesPanel;
import presentationLayer.gui.employeeModule.view.panels.employees.EmployeeCertificationPanel;
import presentationLayer.gui.employeeModule.view.panels.employees.EmployeeDetailsPanel;
import presentationLayer.gui.employeeModule.view.panels.employees.RecruitEmployeePanel;
import presentationLayer.gui.employeeModule.view.panels.employees.UpdateEmployeePanel;
import presentationLayer.gui.employeeModule.view.panels.shifts.*;
import presentationLayer.gui.employeeModule.view.panels.users.AuthorizeUserPanel;
import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.plAbstracts.PanelManager;
import presentationLayer.gui.plAbstracts.interfaces.Panel;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.Link;
import presentationLayer.gui.plUtils.QuickAccess;

public class EmployeeView extends MainWindow {

    private Panel currentPanel;
    private final PanelManager panelManager;
    private final EmployeesControl employeesControl;
    private final ShiftsControl shiftsControl;
    private final String employeeId;

    public EmployeeView(EmployeesControl employeesControl, ShiftsControl shiftsControl, String employeeId) {
        super("Employee Page");
        this.employeesControl = employeesControl;
        this.shiftsControl = shiftsControl;
        this.employeeId = employeeId;
        Colors.colorPalette = Colors.ColorPalette.hr;
        currentPanel = new EmployeesPanel();
        panelManager = new PanelManager(currentPanel);
        super.addUIElement(panelManager);
        super.addUIElement(initQuickAccess());
        super.init();
        super.setVisible(true);
    }

    private QuickAccess initQuickAccess(){
        return new QuickAccess()
                .addCategory("Shifts",
//                        new Link("View Shifts", // + My shifts
//                                () -> setCurrentPanel(new EmployeeShiftsPanel(employeeId, shiftsControl, this))),
                        new Link("Request Shift",
                                () -> setCurrentPanel(new RequestShiftPanel(employeeId, shiftsControl, this))),
                        new Link("Cancel Shift Request",
                                () -> setCurrentPanel(new CancelShiftRequestPanel(employeeId, shiftsControl, this))),
                        new Link("Report Shift Activity",
                                () -> setCurrentPanel(new ShiftActivityPanel(employeeId, shiftsControl, this))),
                        new Link("Apply Cancel Card",
                                () -> setCurrentPanel(new CancelCardPanel(employeeId, shiftsControl, this)))
                )
                .addCategory("Employee Details",
                        new Link("Show my details",
                                () -> setCurrentPanel(new EmployeeDetailsPanel(employeesControl, employeeId)))
                );
    }

    public void setCurrentPanel(Panel panel) {
        panelManager.setPanel(panel);
        currentPanel = panel;
        currentPanel.componentResized(super.container.getSize());
        super.container.revalidate();
    }

    public static void main(String[] args) {
        PresentationFactory factory = new PresentationFactory();
        EmployeesControl employeesControl = factory.employeesControl();
        ShiftsControl shiftsControl = factory.shiftsControl();

        MainWindow mainWindow = new EmployeeView(employeesControl, shiftsControl,"111");
    }
}