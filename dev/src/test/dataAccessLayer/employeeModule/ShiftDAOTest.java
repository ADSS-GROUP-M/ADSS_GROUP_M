package dataAccessLayer.employeeModule;


import dataAccessLayer.DalFactory;
import businessLayer.employeeModule.*;
import dataAccessLayer.dalUtils.DalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

public class ShiftDAOTest {

    ShiftDAO shiftDAO;
    EmployeeDAO empDao;
    Shift shift1;
    Shift shift2;
    Map<Role,List<Employee>> workers;
    Employee employee1;
    Employee employee2;
    Employee employee3;
    Employee employee4;
    private DalFactory factory;

    @BeforeEach
    public void setUp(){ // cleans empDAO, creates 3 employees in DB, sets up 2 of them as workers shift

        try{

            factory = new DalFactory(TESTING_DB_NAME);

            workers = new HashMap<>();
            List<Employee> cashiers = new LinkedList<>();
            List<Employee> generalWorkers = new LinkedList<>();

            shiftDAO = factory.shiftDAO();
            empDao = factory.employeeDAO();

            shift1 = new Shift("branch1",LocalDate.now(), Shift.ShiftType.Evening);
            shift2 = new Shift("branch2",LocalDate.of(1893,2,24), Shift.ShiftType.Morning);

            employee1 = new Employee("abc","123456", "Poalim", 2, LocalDate.now(),"condition","detil");
            employee1.addRole(Role.GeneralWorker);
            empDao.insert(employee1);
//        generalWorkers.add(employee1); // not sure if this needs to be here

            employee2 = new Employee("abc","2088", "Poalim", 2, LocalDate.now(),"condition","detil");
            employee2.addRole(Role.GeneralWorker);
            empDao.insert(employee2);
            generalWorkers.add(employee2);

            employee3 = new Employee("qwerty","1118", "Poalim", 2, LocalDate.now(),"condition","detil");
            employee3.addRole(Role.Cashier);
            empDao.insert(employee3);
            cashiers.add(employee3);

            employee4 = new Employee("four","1122", "Poalim", 32, LocalDate.now(),"conditions","Detedfails");
            employee4.addRole(Role.SecurityGuard);
            empDao.insert(employee4);

            workers.put(Role.GeneralWorker,generalWorkers);
            workers.put(Role.Cashier,cashiers);

            shift1.setShiftWorkers(workers);

            shiftDAO.create(shift1);
            shiftDAO.create(shift2);
        } catch (Exception e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    public void testCreateData(){// creates 2 shifts in DB
        assertThrows(DalException.class, () -> shiftDAO.create(shift1)); // cannot create same object with existing key
    }

    //To test data persistence, run after 'createData'
    @Test
    public void delete(){ // deletes shift from DB
        try{
            shiftDAO.delete(shift1);
            assertNull(shiftDAO.get(shift1.getBranch(), shift1.getShiftDate(), shift1.getShiftType()));
        } catch(Exception e) {
            fail(e);
        }
    }

    //To test data persistence, run after 'createData'
    @Test
    public void get(){// gets the shift from DB and confirms its' state
        try{
           Shift sh = shiftDAO.get(shift1.getBranch(), shift1.getShiftDate(), shift1.getShiftType());
            assertEquals(sh.getShiftDate().getDayOfYear(),shift1.getShiftDate().getDayOfYear());
            assertEquals(sh.getShiftWorkers().get(Role.GeneralWorker).get(0).getId(), shift1.getShiftWorkers().get(Role.GeneralWorker).get(0).getId());
        } catch(Exception e) {
            fail(e);
        }
    }

    //To test data persistence, run after 'createData'
    @Test
    public void update(){
        try{
            shift1 = shiftDAO.get("branch1", shift1.getShiftDate(), shift1.getShiftType());
            HashMap<Role,List<Employee>> newWorkers = new HashMap<>();
            List<Employee> worker = new LinkedList<>();
            worker.add(employee1);
            newWorkers.put(Role.GeneralWorker, worker);
            shift1.setShiftWorkers(newWorkers);
            shift1.setApproved(true);
            shiftDAO.update(shift1);
            Shift s2 = shiftDAO.get("branch1", shift1.getShiftDate(), shift1.getShiftType());
            assertEquals(s2.getShiftWorkers().get(Role.GeneralWorker).get(0).getId(), worker.get(0).getId());
        } catch(Exception e) {
            fail(e);
        }
    }

    //To test data persistence, run after 'createData'
    @Test
    public void update2(){

        try{
            shift1 = shiftDAO.get("branch1", shift1.getShiftDate(), shift1.getShiftType());
            workers = shift1.getShiftWorkers();
            workers.get(Role.GeneralWorker).remove(workers.get(Role.GeneralWorker).get(0)); // removes twoEmployee
            List<Employee> security = new LinkedList<>();
            security.add(employee4);
            workers.put(Role.SecurityGuard, security);
            shiftDAO.update(shift1);
            Shift s2 = shiftDAO.get("branch1", shift1.getShiftDate(), shift1.getShiftType());
            assertEquals(s2.getShiftWorkers().get(Role.GeneralWorker).size(), 0);
            assertEquals(s2.getShiftWorkers().get(Role.SecurityGuard).get(0).getId(), employee4.getId());
            assertTrue(s2.getShiftWorkers().get(Role.SecurityGuard).get(0).getRoles().contains(Role.SecurityGuard));
        } catch(Exception e) {
            fail(e);
        }
    }

    @Test
    public void selectAllCacheTest(){
        try{
            List<Shift> list = shiftDAO.getAll();
            assertEquals(list.size(), 2);
        } catch(Exception e) {
            fail(e);
        }
    }

    @Test
    public void selectAllSQLTest(){ // tries to get all shifts from DB without relying on cache.

        try{
            List<Shift> list = shiftDAO.getAll();
            boolean sFlag = false, noiseFlag = false;
            for(Shift sh: list){
                if(sh.getShiftDate().getDayOfYear() == shift1.getShiftDate().getDayOfYear() && sh.getShiftType() == shift1.getShiftType())
                    sFlag = true;
                if(sh.getShiftDate().getDayOfYear() == shift2.getShiftDate().getDayOfYear() && sh.getShiftType() == shift2.getShiftType())
                    noiseFlag = true;
            }

            assertTrue(sFlag && noiseFlag);
        } catch(Exception e) {
            fail(e);
        }
    }


    @Test
    public void checkCachedObjects() throws Exception{ // tests wether changes in object's state is visible to other dao clients without updating it in the database.

        LocalDate dt = LocalDate.now();
        Shift.ShiftType st = Shift.ShiftType.Morning;
        String branch = "branch1";
        Shift s2 = new Shift("branch1",dt, st);
        s2.setApproved(false);
        Shift s3 = new Shift("branch1",LocalDate.of(2022,3,1), Shift.ShiftType.Morning);
        try{
            shiftDAO.create(s2);
            shiftDAO.create(s3);
            s2.setApproved(true);
            Shift testShift = shiftDAO.get(branch, dt,st);
            assertTrue(testShift.getIsApproved());
        } catch(Exception e) {
            fail(e);
        }
    }
}
