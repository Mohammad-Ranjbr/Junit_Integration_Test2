package com.example.spring_boot_testing.service;

import com.example.spring_boot_testing.exception.ResourceNotFoundException;
import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.repository.EmployeeRepository;
import com.example.spring_boot_testing.service.Impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Makes JUnit 5 know to use Mockito to handle mocks. This annotation ensures
// that the processes required to create and inject mocks are performed correctly.
// Mockito understands that we are using mark annotations to mock the dependencies.
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    private Employee employee;

    // @Mock is an annotation used in the Mockito library to create mock objects.
    // This annotation is typically used to create and set the behavior of mock objects in unit tests.
    @Mock
    private EmployeeRepository employeeRepository;

    // When we want to inject a mocked object into another mocked object, we
    // can use @InjectMocks annotation. @InjectMocks creates the mock object
    // of the class and injects the mocks that are marked with the annotations @Mock into it
    // Mockito creates an instance of the target class.
    // All mock objects and spy objects annotated with @Mock or @Spy in the same test class are injected into the generated instance fields.
    // These injections can be done in three ways:
    // Via constructor (Constructor Injection): If the class has a constructor whose parameters match mock objects, Mockito uses this constructor to create the instance.
    // Through setters (Setter Injection): If the class has setters that match mock objects, Mockito will call these setters.
    // Through fields (Field Injection): If the fields correspond directly to the mock objects, Mockito directly accesses these fields and injects the mock objects.
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setup(){
        /*employeeRepository = Mockito.mock(EmployeeRepository.class);
        /employeeService = new EmployeeServiceImpl(employeeRepository);*/
        employee = Employee.builder()
                .id(1L)
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();
    }

    // JUnit test for saveEmployee method
    @Test
    @DisplayName("JUnit test for saveEmployee method")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        // given - precondition or setup

        // thenReturn : This method is more traditional.
        // After defining a mock behavior, you use this method to specify the value to return.
        // Using thenReturn is simple and straightforward.
        // willReturn : This method follows the BDD (Behavior-Driven Development) style.
        // This method is used in combination with given from BDDMockito.
        // Using willReturn makes tests more readable in BDD scenarios.
        // thenReturn and willReturn are both functionally identical, only changing the coding style.

//        Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
//        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee))
                .willReturn(employee);

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();

    }

    // JUnit test for saveEmployee method witch throes exception
    @Test
    @DisplayName("JUnit test for saveEmployee method witch throes exception")
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when - action or the behavior that we are going test
        // the first parameter is the type of exception that is expected to be thrown,
        // and the second parameter is the code that should be executed and result in that exception being thrown.
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        // then - verify the output
        verify(employeeRepository,never()).save(any(Employee.class));

    }

    // JUnit test for getAllEmployees method (positive scenario)
    @Test
    @DisplayName("JUnit test for getAllEmployees method (positive scenario)")
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Hossein")
                .lastName("Aslani")
                .email("hosseinaslani@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(this.employee,employee));

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

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

    // JUnit test for getAllEmployees method (negative scenario)
    @Test
    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {

        // given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
        Assertions.assertThat(employeeList).isEmpty();

    }

    // JUnit test for getEmployeeById method
    @Test
    @DisplayName("JUnit test for getEmployeeById method")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {

        // given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId());

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mohammad");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Ranjbar");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("mohammadranjbar@gmail.com");

    }

    // JUnit test for updateEmployee method
    @Test
    @DisplayName("JUnit test for updateEmployee method")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Hossein");
        employee.setEmail("hosseinranjbar.mmr91@gmail.com");

        // when - action or the behavior that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee).isNotNull();
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("hosseinranjbar.mmr91@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Hossein");

    }

    // JUnit test for deleteEmployee method
    @Test
    @DisplayName("JUnit test for deleteEmployee method")
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {

        // given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // when - action or the behavior that we are going test
        employeeService.deleteEmployee(employee.getId());

        // then - verify the output
        verify(employeeRepository,times(1)).deleteById(employee.getId());

    }

}
