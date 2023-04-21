package dataAccessLayerTests;


import DataAccessLayer.ShiftDAO;
import com.google.gson.reflect.TypeToken;
import employeeModule.BusinessLayer.Employees.Authorization;
import employeeModule.BusinessLayer.Employees.Role;
import employeeModule.BusinessLayer.Employees.Shift;
import employeeModule.BusinessLayer.Employees.User;
import employeeModule.ServiceLayer.Objects.SEmployee;
import employeeModule.ServiceLayer.Objects.SShift;
import employeeModule.ServiceLayer.Objects.SShiftType;
import employeeModule.ServiceLayer.Services.EmployeesService;
import employeeModule.ServiceLayer.Services.UserService;
import employeeModule.employeeUtils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Response;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    ShiftDAO dao;
    Shift s;
    @BeforeEach
    public void setUp() throws Exception {
        s = new Shift(LocalDate.now(), Shift.ShiftType.Evening);
    }

    @Test
    public void create() throws Exception {
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        dao.deleteAll();

        try{
            dao.create(s,"branch1");
            try{
                dao.create(s,"branch1"); // cannot create same object with existing key
                assertTrue(false);
            }catch(Exception e){ assertTrue(true);}
            assertTrue(true);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }
    @Test
    public void delete() throws Exception {
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
        //dao.deleteAll();

        try{
            dao.delete(s,"branch1");
            assertTrue(true);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }
    @Test
    public void get() throws Exception {
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
       // dao.deleteAll();

        try{
           Shift sh = dao.get(LocalDate.now(), Shift.ShiftType.Evening,"branch1");
           System.out.println(sh.getShiftDate());
            assertTrue(sh!=null);
        } catch(Exception e) {e.printStackTrace(); assertTrue(false);}
    }
    @Test
    public void update() throws Exception {
        dao = ShiftDAO.getInstance();
        assertTrue(dao!=null);
       // dao.deleteAll();

        try{
            s = dao.get(s.getShiftDate(),s.getShiftType(),"branch1");
            s.setApproved(true);
            dao.update(s,"branch1");
            assertTrue(true);
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
