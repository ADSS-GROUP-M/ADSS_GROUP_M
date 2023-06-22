package presentationLayer.gui.employeeModule.view;

import presentationLayer.DataGenerator;
import presentationLayer.PresentationFactory;
import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.controller.UsersControl;
import presentationLayer.gui.employeeModule.view.panels.EmployeesPanel;
import presentationLayer.gui.employeeModule.view.panels.employees.EmployeeCertificationPanel;
import presentationLayer.gui.employeeModule.view.panels.employees.RecruitEmployeePanel;
import presentationLayer.gui.employeeModule.view.panels.employees.UpdateEmployeePanel;
import presentationLayer.gui.employeeModule.view.panels.shifts.CreateShiftsPanel;
import presentationLayer.gui.employeeModule.view.panels.shifts.ViewShiftsPanel;
import presentationLayer.gui.employeeModule.view.panels.users.AuthorizeUserPanel;
import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.plAbstracts.PanelManager;
import presentationLayer.gui.plAbstracts.interfaces.Panel;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.Link;
import presentationLayer.gui.plUtils.QuickAccess;

import javax.swing.*;

public class HRManagerView extends MainWindow {

    private Panel currentPanel;
    private final PanelManager panelManager;
    private final EmployeesControl employeesControl;
    private final ShiftsControl shiftsControl;
    private final UsersControl usersControl;

    public HRManagerView(EmployeesControl employeesControl, ShiftsControl shiftsControl, UsersControl usersControl) {
        super("HRManager Page");
        this.employeesControl = employeesControl;
        this.shiftsControl = shiftsControl;
        this.usersControl = usersControl;
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
                .addCategory("Employees Management",
                        new Link("Recruit Employee",
                                () -> setCurrentPanel(new RecruitEmployeePanel(employeesControl))),
                        new Link("Update Employee",
                                () -> setCurrentPanel(new UpdateEmployeePanel(employeesControl))),
                        new Link("Certify Employee", // + Remove employee certification
                                () -> setCurrentPanel(new EmployeeCertificationPanel(employeesControl)))
                )
                .addCategory("Shifts Management",
                        new Link("View Shifts",
                                () -> setCurrentPanel(new ViewShiftsPanel(shiftsControl, this))), // + Update Shifts, Approve Shifts, Delete Shifts
                        new Link("Create Shifts",
                                () -> setCurrentPanel(new CreateShiftsPanel(shiftsControl)))
                )
                .addCategory("User Management",
                        new Link("Authorize User",
                                () -> setCurrentPanel(new AuthorizeUserPanel(usersControl))))
                .addCategory("Data Generation",
                        new Link("Generate Data",
                                () -> JOptionPane.showMessageDialog(null,new DataGenerator().generateData()))
                );
    }

    public void setCurrentPanel(Panel panel) {
        panelManager.setPanel(panel);
        currentPanel = panel;
        currentPanel.componentResized(panelManager.getComponent().getSize());
        super.container.revalidate();
    }

    public static void main(String[] args) {
        PresentationFactory factory = new PresentationFactory();
        EmployeesControl employeesControl = factory.employeesControl();
        ShiftsControl shiftsControl = factory.shiftsControl();
        UsersControl usersControl = factory.usersControl();

        MainWindow mainWindow = new HRManagerView(employeesControl, shiftsControl, usersControl);
    }
}