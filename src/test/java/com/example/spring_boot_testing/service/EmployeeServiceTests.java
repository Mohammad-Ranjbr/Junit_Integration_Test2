package com.example.spring_boot_testing.service;

import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.repository.EmployeeRepository;
import com.example.spring_boot_testing.service.Impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
