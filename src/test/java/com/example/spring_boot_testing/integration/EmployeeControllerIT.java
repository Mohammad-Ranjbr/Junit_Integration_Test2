package com.example.spring_boot_testing.integration;

import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

// Spring Boot provides @SpringBootTest annotation for Integration testing. This
// annotation creates an application context and loads full application context.
// @SpringBootTest will bootstrap the full application context, which means we
// can @Autowire any bean that's picked up by component scanning into our test.

// By default, @SpringBootTest does not start a real server. To start the server in tests,
// you can use the webEnvironment property. This feature has several options:
// MOCK (default): In this mode, an ApplicationContext is loaded for the web,
// but the web environment is simulated and the real server is not started. These tests,
// which require an environment with a real web environment, are useful.
// RANDOM_PORT: In this case, a WebServerApplicationContext is loaded and a real web environment is created.
// The real server starts on a random port. This option is usually used for integration tests (integration tests) that require a real environment.
// DEFINED_PORT: In this mode, a real server is set up like before, but the server listens on a port already defined (usually in the application).
// None: In this case, only an ApplicationContext is loaded, but no web environment is created. This option is suitable for tests that do not require a server.

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// This annotation allows you to create a real web environment with an embedded server running on a random port.
// This is usually used for integration tests where you need a real server running so you can test the whole application or large parts of it.
// @AutoConfigureMockMvc: This annotation allows you to automatically configure MockMvc,
// which is commonly used to test controllers without having to run a real server.

// @AutoConfigureMockMvc is a good choice if you want to test your Spring MVC controllers without having to set up a full server.
// This tool allows you to send simulated HTTP requests (like GET, POST, PUT, etc.) to your controllers and check their results.
// Because @AutoConfigureMockMvc doesn't initialize the actual server and only loads Spring MVC's controllers and structure,
// tests run much faster. So, if you want to run your tests faster, you can use this annotation.

// Integration test for controller layer

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("Hossein")
                .lastName("Ranjbar")
                .email("hosseinranjbar@gmail.com")
                .build();
        List<Employee> employeeList = List.of(employee1, employee2);
        employeeRepository.saveAll(employeeList);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmptyObject() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {

        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Hossein")
                .lastName("Aslani")
                .email("hossein@gmail.com")
                .build();


        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Hossein")
                .lastName("Aslani")
                .email("hossein@gmail.com")
                .build();

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{

        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",savedEmployee.getId()));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

}
