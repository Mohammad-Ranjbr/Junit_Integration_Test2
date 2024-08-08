package com.example.spring_boot_testing.controller;

import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

// The @WebMvcTest annotation in Spring Boot is specifically designed to test the web layer (controllers).
// This annotation brings only the dependencies related to the web layer and does not load other layers such as the service layer by default.
// Typically, the controller layer has dependencies on the service layer,
// and you need to provide these dependencies manually for your tests to work properly.
// Mock objects are usually used for the service layer. For this you can use tools like Mockito.
@WebMvcTest
public class EmployeeControllerTests {

    // To call Rest Api
    @Autowired
    private MockMvc mockMvc;

    // @Mock (from Mockito)
    // Use in Unit Tests: The @Mock annotation comes from the Mockito library and is used to create mock objects in unit tests.
    // This annotation mocks dependencies so that you can test their methods and behavior without having to run the actual code.
    // Manual injection: If you use @Mock, you must inject the mock object into the class yourself, for example using @InjectMocks or constructor injection.
    // Independent of Spring: This annotation is completely independent of Spring and can be used in any type of test.
    // @MockBean (from Spring Boot)
    // Use in Integration Tests or Web layer tests: @MockBean is a Spring Boot annotation and is typically used in tests run with @SpringBootTest or @WebMvcTest.
    // Automatic injection into Spring Context: When you use @MockBean, Spring automatically injects the created mock object into its own context,
    // so wherever that dependency is used in the Spring context, the mock object will be used.
    // Suitable for testing controllers and services: This annotation is mostly used when you want to mock a part of the system, but test other parts in a real way.
    // Scope of use: @Mock is suitable for unit tests and @MockBean for tests where you need the Spring Context to be fully or partially loaded.
    // Injection: @Mock requires manual injection, while @MockBean automatically injects the mock object into the Spring Context.
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // JUnit test for create employee api
    @Test
    @DisplayName("JUnit test for create employee api")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();

        // Whenever the saveEmployee method is called, instead of actually executing the method's logic,
        // the return value is the argument passed to the method.
        // willAnswer((invocation) -> invocation.getArgument(0)):
        // This method specifies what to return when the saveEmployee method is called.
        // In this case, willAnswer is a lambda expression that uses invocation (which has information about the method invocation).
        // invocation.getArgument(0) returns the value of the first argument given to the saveEmployee method.
        // Simplicity: willReturn is simpler and is suitable for cases where you need a static, non-dynamic response.
        // Flexibility: willAnswer is more flexible and is used for situations where the answer needs to change based on specific inputs or conditions.
        // In short, willReturn is used for simple and static responses, while willAnswer is suitable for more complex and dynamic situations.

        // You use ArgumentMatchers when you mock a method with Mockito and want the method
        // to work with any value that matches a certain condition instead of a specific value.
        // When using ArgumentMatchers, you must be careful that if you use a matcher in a method, you must use matchers for all arguments,
        // or at least use an equivalent matcher instead of using specific values directly.
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // willReturn is perfect for when you need to return a constant value as a response, regardless of what input is given to the method.
        // Here, no matter what Employee is passed to saveEmployee , This behavior is fixed and does not depend on the input.
        //BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
        //        .willReturn(employee);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                // Hamcrest is commonly used with JUnit and other testing frameworks for making
                // assertions. Specifically, instead of using JUnit’s numerous assert methods, we only
                // use the API's single assertThat statement with appropriate matchers.
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(employee.getEmail())))
                .andDo(MockMvcResultHandlers.print());
        verify(employeeService,times(1)).saveEmployee(any(Employee.class));

    }

    // JUnit test for get all employees api
    @Test
    @DisplayName("JUnit test for get all employees api")
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
        List<Employee> employeeList = List.of(employee1,employee2);
        given(employeeService.getAllEmployees()).willReturn(employeeList);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(employeeList.size())))
                .andDo(MockMvcResultHandlers.print());

    }

    // JUnit test for get employee by id api (positive scenario)
    @Test
    @DisplayName("JUnit test for get employee by id api (positive scenario)")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(employee);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",employeeId));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(employee.getEmail())))
                .andDo(MockMvcResultHandlers.print());

    }

}
