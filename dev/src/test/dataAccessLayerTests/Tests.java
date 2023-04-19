package dataAccessLayerTests;


import DataAccessLayer.UserDAO;
import DataAccessLayer.UserDTO;
import com.google.gson.reflect.TypeToken;
import employeeModule.BusinessLayer.Employees.Authorization;
import employeeModule.BusinessLayer.Employees.Role;
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
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    UserDTO userDto;
    UserDAO dao;
    @BeforeEach
    public void setUp() throws Exception {
        dao = new UserDAO();
        userDto = new UserDTO(dao,3,"sd", "br", 1);
    }
    @Test
    public void situation1() { //
        try {
            userDto.setUsername("abc");
            userDto.setPassword(("gfds"));
            userDto.setLoggedIn(true);
        } catch (Exception e){e.printStackTrace();}


    }

}
