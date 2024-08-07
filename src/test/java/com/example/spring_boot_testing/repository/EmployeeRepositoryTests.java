package com.example.spring_boot_testing.repository;

import com.example.spring_boot_testing.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private Employee employee;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeRepositoryTests(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    // When this annotation is added to a method, that method is executed before any test is executed in the same test class.
    // This annotation is typically used to perform preparatory operations that must be performed before each test,
    // such as initializing variables or setting up the environment.
    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
    }

    // JUnit test for save employee operation
    @Test
    @DisplayName("JUnit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Mohammad")
//                .lastName("Ranjbar")
//                .email("mohammadranjbar@gmail.com")
//                .build();

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for get all employees operation
    @Test
    @DisplayName("JUnit test for get all employees operation")
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {

        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("Hossein")
                .lastName("Aslani")
                .email("hosseinaslani@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);

        Assertions.assertThat(employeeList.get(0).getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(employeeList.get(0).getLastName()).isEqualTo( "Ranjbar");
        Assertions.assertThat(employeeList.get(0).getEmail()).isEqualTo("mohammadranjbar@gmail.com");

        // isIn is used to check if a value exists in a set of values. This method checks if a particular value exists in the given set of values.
        Assertions.assertThat(employeeList.get(1).getFirstName()).isIn("Mohammad","Hossein");
        Assertions.assertThat(employeeList.get(1).getLastName()).isIn("Aslani", "Ranjbar");
        Assertions.assertThat(employeeList.get(1).getEmail()).isIn("hosseinaslani@gmail.com", "mohammadranjbar@gmail.com");


    }

    // JUnit test for get employee by id
    @Test
    @DisplayName("JUnit test for get employee by id")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee getEmployee = null;
        if(employeeRepository.findById(employee.getId()).isPresent()){
            getEmployee = employeeRepository.findById(employee.getId()).get();
        }

        // then - verify the output
        Assertions.assertThat(getEmployee).isNotNull();
        Assertions.assertThat(getEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(getEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(getEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for get employee by email operation
    @Test
    @DisplayName("JUnit test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee getEmployee = null;
        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()){
            getEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        }

        // then - verify the output
        Assertions.assertThat(getEmployee).isNotNull();
        Assertions.assertThat(getEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(getEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(getEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for update employee operation
    @Test
    @DisplayName("JUnit test for update employee operation")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup
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

    // JUnit test for delete employee operation
    @Test
    @DisplayName("JUnit test for delete employee operation")
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        employeeRepository.delete(employee);
        Optional<Employee> deleteEmployee = employeeRepository.findById(employee.getId());

        // then - verify the output
        Assertions.assertThat(deleteEmployee).isEmpty();

    }

    // JUnit test for custom query using JPQL with index params
    @Test
    @DisplayName("JUnit test for custom query using JPQL with index params")
    public void givenFirstNameAndLastName_whenFindByJPQLIndexParams_thenReturnEmployeeObject() {

        // given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "Mohammad";
        String lastName = "Ranjbar";

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLIndexParams(firstName,lastName);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for custom query using JPQL with named params
    @Test
    @DisplayName("JUnit test for custom query using JPQL with named params")
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {

        // given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "Mohammad";
        String lastName = "Ranjbar";

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName,lastName);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for custom query using native SQL with index
    @Test
    @DisplayName("JUnit test for custom query using native SQL with index")
    public void givenFirstNameAndLastName_whenFindByNativeSQLIndexParams_thenReturnEmployeeObject() {

        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLIndexParams(employee.getFirstName(),employee.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for custom query using native SQL with named params
    @Test
    @DisplayName("JUnit test for custom query using native SQL with index")
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {

        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(),employee.getLastName());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

}
