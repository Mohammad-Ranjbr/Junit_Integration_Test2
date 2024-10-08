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

// Isolation of external dependencies:
// In integration tests, you often need to simulate external dependencies such as databases, messaging systems,
// or other services. Using a real database or other services during testing may cause unwanted dependencies or conflicts between tests.
// Testcontainers allows you to run each of these dependencies in a separate, isolated Docker container.
// This ensures that the test environment is always the same and independent of other tests or system settings.
// Integration of tests with the production environment:
// By using Docker containers, you can use the same versions of databases or services that are used in the
// production environment in your tests. This will help your tests to be as close to real conditions as possible
// and avoid problems caused by environmental differences.
// Ease of managing test environments:
// Without Testcontainers, you may need to manually install and configure external dependencies for each test.
// This is time-consuming and error-prone, and may lead to interference from different software versions.
// Testcontainers automates this process: containers are quickly started and cleaned up automatically after the tests are finished,
// without the need to manually make any special settings.
// CI/CD support:
// In CI/CD environments (such as Jenkins, GitLab CI, GitHub Actions, etc.), using Testcontainers helps tests
// run identically and independently of the environment. Because of the use of Docker, the tests can run correctly on any system that has Docker installed.
// This integration ensures that your tests are always reliable and repeatable.
// Post-test cleanup: After tests are completed, Testcontainers automatically cleans up Docker containers, leaving no trace of the test environment.
// In short, the main reason for using Testcontainers is to provide an isolated, repeatable,
// close-to-production testing environment that easily and automatically manages external dependencies.
// This tool helps to perform reliable and effective integration tests simply and with high accuracy, without the need to manually set up the test environment.

// The @Testcontainers annotation is used to use the Testcontainers library in Java tests.
// This annotation specifies that the corresponding test class uses Testcontainers and should launch and manage Docker containers as part of the test environment.
// Uses of @Testcontainers:
// Enabling Testcontainers in tests:
// By using @Testcontainers, you tell Spring (or other Java testing frameworks) that this test class uses
// Testcontainers and that the corresponding Docker containers are required to be started.
// Setting up and managing containers:
// In any test that uses Testcontainers, the Docker containers defined in that test are automatically managed and
// controlled by Testcontainers. The @Testcontainers annotation enables this process.
// Coordination with other test annotations:
// @Testcontainers are usually used in conjunction with other annotations such as @Container to define and configure each specific container.
// This annotation allows you to manage containers as a surface class (initialized for all tests) or a surface method (initialized for each test).

// Autostart: Testcontainers automatically starts Docker containers before running tests and stops them after they finish.
// Integration with other tools: @Testcontainers works well with other Java testing tools like JUnit, Spring Boot, etc.,
// allowing you to write more complex and complete integration tests.


// In this case, we manually perform the life cycle of the container, that's why we can delete the @Testcontainers annotation,
// because we do it manually, and the @Testcontainers annotation is for the mode that manages the Testcontainers itself.

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerITests extends AbstractContainerBaseTest {

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

    // will be started before and stopped after each test method
    /*@Container
    private static final PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("ems")
            .withUsername("foo")
            .withPassword("secret");*/

    // The @DynamicPropertySource annotation is used in Spring Boot to dynamically set configuration values during test execution.
    // This feature is especially useful when you want to automatically and dynamically set configuration
    // values that depend on the environment or depend on external resources.
    // In your example, @DynamicPropertySource is used to set database values (such as URL, username and password) dynamically.
    // These values are taken from a PostgreSQL container started by Testcontainers.
    // @DynamicPropertySource: This annotation tells Spring Boot that the specified method (dynamicPropertySource) should be called during the test startup
    // process to set some configuration values dynamically.
    // DynamicPropertyRegistry: This class provides an interface that allows you to dynamically register Spring configuration values.
    // registry.add(...): These methods are used to add or overwrite configuration values in the DynamicPropertyRegistry.
    // For example, registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl); Sets the value of
    // spring.datasource.url to the dynamic database URL managed by Testcontainers.
    // Application: Communication with external sources during testing:
    // Let's say you've set up a PostgreSQL container for your tests with Testcontainers. Since the database URL, username,
    // and password may change each time the test is run (because the container may be started on a different port), you need to set these values dynamically.
    // Automatically set Spring configuration values:
    // Instead of manually and statically defining these values in application.properties or application.yml,
    // this code allows Spring Boot to automatically set these values at runtime.
    // Instead of storing configuration values statically in configuration files (such as application.properties), you can set them based on runtime conditions.

    /*@DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password",postgresqlContainer::getPassword);
    }*/

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        /*System.out.println("Postgres container database name: " + postgresqlContainer.getDatabaseName());
        System.out.println("Postgres container database url: " + postgresqlContainer.getJdbcUrl());
        System.out.println("Postgres container username: " + postgresqlContainer.getUsername());
        System.out.println("Postgres container password: " + postgresqlContainer.getPassword());*/

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
        long employeeId = 10L;
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
        long employeeId = 10L;
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
