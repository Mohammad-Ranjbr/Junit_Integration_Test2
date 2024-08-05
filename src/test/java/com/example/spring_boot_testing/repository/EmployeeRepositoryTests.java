package com.example.spring_boot_testing.repository;

import com.example.spring_boot_testing.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import static org.assertj.core.api.Assertions.assertThat;

// Spring Boot provides the @DataJpaTest annotation to test the persistence
// layer components that will autoconfigure in-memory embedded database for testing purposes.
// The @DataJpaTest annotation doesn’t load other Spring beans
// (@Components, @Controller, @Service, and annotated beans) into ApplicationContext.
// By default, it scans for @Entity classes and configure Spring Data JPA
// repositories annotated with @Repository annotation By default,
// tests annotated with @DataJpaTest are transactional and roll back at the end of each test.

/* @Test
 public void given_when_then() {
 given - precondition or setup
 when - action or the behaviour we’re testing
 then - verify the output
//} */

@DataJpaTest
public class EmployeeRepositoryTests {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeRepositoryTests(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    // JUnit test for save employee operation
    @Test
    @DisplayName("JUnit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

}
