package com.example.spring_boot_testing.integration;

import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("JUnit test for create employee api")
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(employee.getEmail())))
                .andDo(MockMvcResultHandlers.print());

    }

}
