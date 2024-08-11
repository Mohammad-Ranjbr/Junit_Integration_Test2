package com.example.spring_boot_testing.repository;

import com.example.spring_boot_testing.integration.AbstractContainerBaseTest;
import com.example.spring_boot_testing.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIT extends AbstractContainerBaseTest {

    private Employee employee;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeRepositoryIT(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
        employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
    }
    // JUnit test for save employee operation
    //@DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");
    }


    // JUnit test for get all employees operation
    @DisplayName("JUnit test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){
        // given - precondition or setup

        Employee employee2 = Employee.builder()
                .firstName("Hossein")
                .lastName("Aslani")
                .email("hosseinaslani@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when -  action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

        Assertions.assertThat(employeeList.get(0).getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(employeeList.get(0).getLastName()).isEqualTo( "Ranjbar");
        Assertions.assertThat(employeeList.get(0).getEmail()).isEqualTo("mohammadranjbar@gmail.com");

        // isIn is used to check if a value exists in a set of values. This method checks if a particular value exists in the given set of values.
        Assertions.assertThat(employeeList.get(1).getFirstName()).isIn("Mohammad","Hossein");
        Assertions.assertThat(employeeList.get(1).getLastName()).isIn("Aslani", "Ranjbar");
        Assertions.assertThat(employeeList.get(1).getEmail()).isIn("hosseinaslani@gmail.com", "mohammadranjbar@gmail.com");

    }

    // JUnit test for get employee by id operation
    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        Employee getEmployee = null;
        if(employeeRepository.findById(employee.getId()).isPresent()){
            getEmployee = employeeRepository.findById(employee.getId()).get();
        }

        // then - verify the output
        assertThat(getEmployee).isNotNull();
        Assertions.assertThat(getEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(getEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(getEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        Employee getEmployee = null;
        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()){
            getEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        }

        // then - verify the output
        assertThat(getEmployee).isNotNull();
        Assertions.assertThat(getEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(getEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(getEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
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
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    // JUnit test for custom query using JPQL with index
    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "Mohammad";
        String lastName = "Ranjbar";

        // when -  action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLIndexParams(firstName, lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for custom query using JPQL with Named params
    @DisplayName("JUnit test for custom query using JPQL with Named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);
        String firstName = "Mohammad";
        String lastName = "Ranjbar";

        // when -  action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for custom query using native SQL with index
    @DisplayName("JUnit test for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLIndexParams(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for custom query using native SQL with named params
    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

}
