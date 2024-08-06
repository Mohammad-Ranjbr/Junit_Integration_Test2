package com.example.spring_boot_testing.repository;

import com.example.spring_boot_testing.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
// import static org.assertj.core.api.Assertions.assertThat;

// Spring Boot provides the @DataJpaTest annotation to test the persistence
// layer components that will autoconfigure in-memory embedded database for testing purposes.
// The @DataJpaTest annotation doesn’t load other Spring beans
// (@Components, @Controller, @Service, and annotated beans) into ApplicationContext.
// By default, it scans for @Entity classes and configure Spring Data JPA
// repositories annotated with @Repository an notation By default,
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

    // JUnit test for get all employees operation
    @Test
    @DisplayName("JUnit test for get all employees operation")
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {

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

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);

    }

    // JUnit test for get employee by id
    @Test
    @DisplayName("JUnit test for get employee by id")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();

        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee getEmployee = null;
        if(employeeRepository.findById(employee.getId()).isPresent()){
            getEmployee = employeeRepository.findById(employee.getId()).get();
        }

        // then - verify the output
        Assertions.assertThat(getEmployee).isNotNull();

    }

    // JUnit test for get employee by email operation
    @Test
    @DisplayName("JUnit test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();

        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee getEmployee = null;
        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()){
            getEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        }

        // then - verify the output
        Assertions.assertThat(getEmployee).isNotNull();
        Assertions.assertThat(getEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for update employee operation
    @Test
    @DisplayName("JUnit test for update employee operation")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();

        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee getEmployee = null;
        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()){
            getEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        }
        Objects.requireNonNull(getEmployee).setEmail("mohammadranjbar.mmr81@gmail.com");
        getEmployee.setFirstName("Hasan");
        Employee updatedEmployee = employeeRepository.save(getEmployee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee).isNotNull();
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("mohammadranjbar.mmr81@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Hasan");

    }

}
