package dataAccessLayerTests;


import DataAccessLayer.employeeModule.EmployeeDAO;
import DataAccessLayer.employeeModule.ShiftDAO;
import employeeModule.BusinessLayer.Employees.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    ShiftDAO dao;
    EmployeeDAO empDao;
    Shift s;
    HashMap<Role,List<Employee>> workers;
    Employee oneEmployee;
    Employee twoEmployee;
    Employee threeEmployee;
    @BeforeEach
    public void setUp() throws Exception { // cleans empDAO, creates 3 employees in DB, sets up 2 of them as workers shift
        empDao = EmployeeDAO.getInstance();
        empDao.deleteAll();
        s = new Shift(LocalDate.now(), Shift.ShiftType.Evening);
        workers = new HashMap<>();
        List<Employee> generalWorkers = new LinkedList<>();
        twoEmployee = new Employee("abc","2088",2,"Poalim",LocalDate.now(),"condition","detil");
        generalWorkers.add(twoEmployee);
        empDao.create(twoEmployee);
        workers.put(Role.GeneralWorker,generalWorkers);
        List<Employee> cashiers = new LinkedList<>();
        threeEmployee = new Employee("qwerty","1118",2,"Poalim",LocalDate.now(),"condition","detil");
        cashiers.add(threeEmployee);
        empDao.create(threeEmployee);
        workers.put(Role.Cashier,cashiers);
        s.setShiftWorkers(workers);
        oneEmployee = new Employee("abc","123456",2,"Poalim",LocalDate.now(),"condition","detil");
        empDao.create(oneEmployee);
    }

    @Test
    public void create() throws Exception {//resets shifts table, creates 2 shifts in DB
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        dao.deleteAll();
        Shift noise = new Shift(LocalDate.of(1893,2,24), Shift.ShiftType.Morning);
        try{
            dao.create(noise,"branch2");
            dao.create(s,"branch1");
            try{
                dao.create(s,"branch1"); // cannot create same object with existing key
                assertTrue(false);
            }catch(Exception e){ assertTrue(true);}
            assertTrue(true);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }

    //Run only after 'create'.
    @Test
    public void delete() throws Exception { // deletes shift from DB
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        //dao.deleteAll();

        try{
            dao.delete(s,"branch1");
            assertTrue(dao.get(s.getShiftDate(),s.getShiftType(),s.getBranch()) == null);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }

    //Run after 'create'.
    @Test
    public void get() throws Exception {// gets the shift from DB and confirms its' state
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        try{
           Shift sh = dao.get(s.getShiftDate(), s.getShiftType(),s.getBranch());
            assertTrue(sh.getShiftDate().getDayOfYear() == s.getShiftDate().getDayOfYear());
            assertTrue(sh.getShiftWorkers().get(Role.GeneralWorker).get(0).getId() == s.getShiftWorkers().get(Role.GeneralWorker).get(0).getId());
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }
    @Test
    public void update() throws Exception {
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
       // dao.deleteAll();

        try{
            s = dao.get(s.getShiftDate(),s.getShiftType(),"branch1");
            HashMap<Role,List<Employee>> newWorkers = new HashMap<>();
            List<Employee> worker = new LinkedList<>();
            worker.add(oneEmployee);
            newWorkers.put(Role.GeneralWorker, worker);
            s.setShiftWorkers(newWorkers);
            s.setApproved(true);
            dao.update(s,"branch1");
            Shift s2 = dao.get(s.getShiftDate(),s.getShiftType(),"branch1");
            assertTrue(s2.getShiftWorkers().get(Role.GeneralWorker).get(0).getId() == worker.get(0).getId());
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }
    @Test
    public void selectAll() throws Exception{
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        dao.deleteAll();
        Shift s2 = new Shift(LocalDate.now(), Shift.ShiftType.Morning);
        Shift s3 = new Shift(LocalDate.of(2022,3,1), Shift.ShiftType.Morning);
        try{
            dao.create(s,"branch1");
            dao.create(s2,"branch1");
            dao.create(s3,"branch1");
            List<Shift> list = dao.getAll();
            for(Shift sh: list){
                System.out.println(sh);
            }

            assertTrue(list.size() == 3);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }

    @Test
    public void checkCachedObjects() throws Exception{
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        dao.deleteAll();
        LocalDate dt = LocalDate.now();
        Shift.ShiftType st = Shift.ShiftType.Morning;
        String branch = "branch1";
        Shift s2 = new Shift(dt, st);
        s2.setApproved(false);
        Shift s3 = new Shift(LocalDate.of(2022,3,1), Shift.ShiftType.Morning);
        try{
            dao.create(s,"branch1");
            dao.create(s2,"branch1");
            dao.create(s3,"branch1");
            s2.setApproved(true);
            Shift testShift = dao.get(dt,st,branch);
            assertTrue(testShift.getIsApproved() == true);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }


}
