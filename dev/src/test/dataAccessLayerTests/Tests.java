package dataAccessLayerTests;


import dataAccessLayer.employeeModule.EmployeeDAO;
import dataAccessLayer.employeeModule.ShiftDAO;
import businessLayer.employeeModule.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    ShiftDAO dao;
    EmployeeDAO empDao;
    Shift s;
    Shift noise;
    Map<Role,List<Employee>> workers;
    Employee oneEmployee;
    Employee twoEmployee;
    Employee threeEmployee;
    Employee fourthEmployee;

    @BeforeEach
    public void setUp() throws Exception { // cleans empDAO, creates 3 employees in DB, sets up 2 of them as workers shift
        empDao = EmployeeDAO.getInstance();
        empDao.deleteAll();
        s = new Shift(LocalDate.now(), Shift.ShiftType.Evening);
        workers = new HashMap<>();
        List<Employee> generalWorkers = new LinkedList<>();
        twoEmployee = new Employee("abc","2088", "Poalim", 2, LocalDate.now(),"condition","detil");
        twoEmployee.addRole(Role.GeneralWorker);
        generalWorkers.add(twoEmployee);
        empDao.insert(twoEmployee);
        workers.put(Role.GeneralWorker,generalWorkers);
        List<Employee> cashiers = new LinkedList<>();
        threeEmployee = new Employee("qwerty","1118", "Poalim", 2, LocalDate.now(),"condition","detil");
        threeEmployee.addRole(Role.Cashier);
        cashiers.add(threeEmployee);
        empDao.insert(threeEmployee);
        workers.put(Role.Cashier,cashiers);
        s.setShiftWorkers(workers);
        oneEmployee = new Employee("abc","123456", "Poalim", 2, LocalDate.now(),"condition","detil");
        oneEmployee.addRole(Role.GeneralWorker);
        empDao.insert(oneEmployee);
        fourthEmployee = new Employee("four","1122", "Poalim", 32, LocalDate.now(),"conditions","Detedfails");
        fourthEmployee.addRole(Role.SecurityGuard);
        empDao.insert(fourthEmployee);
        noise = new Shift(LocalDate.of(1893,2,24), Shift.ShiftType.Morning);
        createData(); // To test data persistence, comment this line and run the tests separately after creating the data.
    }

    private void createData() throws Exception {
        dao = ShiftDAO.getInstance();
        dao.deleteAll();
        dao.create(noise, "branch2");
        dao.create(s,"branch1");
    }

    @Test
    public void testCreateData() throws Exception {//resets shifts table, creates 2 shifts in DB
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        dao.deleteAll();
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

    //To test data persistence, run after 'createData'
    @Test
    public void delete() throws Exception { // deletes shift from DB
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        try{
            dao.delete(s,"branch1");
            assertTrue(dao.get(s.getShiftDate(),s.getShiftType(),s.getBranch()) == null);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }

    //To test data persistence, run after 'createData'
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

    //To test data persistence, run after 'createData'
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

    //To test data persistence, run after 'createData'
    @Test
    public void update2() throws Exception {
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        // dao.deleteAll();

        try{
            s = dao.get(s.getShiftDate(),s.getShiftType(),"branch1");
            workers = s.getShiftWorkers();
            workers.get(Role.GeneralWorker).remove(workers.get(Role.GeneralWorker).get(0)); // removes twoEmployee
            List<Employee> security = new LinkedList<>();
            security.add(fourthEmployee);
            workers.put(Role.SecurityGuard, security);
            dao.update(s,"branch1");
            Shift s2 = dao.get(s.getShiftDate(),s.getShiftType(),"branch1");
            assertTrue(s2.getShiftWorkers().get(Role.GeneralWorker).size() == 0);
            assertTrue(s2.getShiftWorkers().get(Role.SecurityGuard).get(0).getId() == fourthEmployee.getId());
            assertTrue(s2.getShiftWorkers().get(Role.SecurityGuard).get(0).getRoles().contains(Role.SecurityGuard));
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }

    @Test
    public void selectAllCacheTest() throws Exception{
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
            assertTrue(list.size() == 3);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }

    //To test data persistence, run after 'createData'
    @Test
    public void selectAllSQLTest() throws Exception{ // tries to get all shifts from DB without relying on cache.
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        //dao.deleteAll();
        try{
            List<Shift> list = dao.getAll();
            boolean sFlag = false, noiseFlag = false;
            for(Shift sh: list){
                if(sh.getShiftDate().getDayOfYear() == s.getShiftDate().getDayOfYear() && sh.getShiftType() == s.getShiftType())
                    sFlag = true;
                if(sh.getShiftDate().getDayOfYear() == noise.getShiftDate().getDayOfYear() && sh.getShiftType() == noise.getShiftType())
                    noiseFlag = true;
            }

            assertTrue(sFlag && noiseFlag);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }


    @Test
    public void checkCachedObjects() throws Exception{ // tests wether changes in object's state is visible to other dao clients without updating it in the database.
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
