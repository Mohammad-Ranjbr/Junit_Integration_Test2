package com.example.spring_boot_testing.service;

import com.example.spring_boot_testing.model.Employee;
import com.example.spring_boot_testing.repository.EmployeeRepository;
import com.example.spring_boot_testing.service.Impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

public class EmployeeServiceTests {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @BeforeEach
    public void setup(){
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    // JUnit test for saveEmployee method
    @Test
    @DisplayName("JUnit test for saveEmployee method")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Mohammad")
                .lastName("Ranjbar")
                .email("mohammadranjbar@gmail.com")
                .build();

        // thenReturn : This method is more traditional.
        // After defining a mock behavior, you use this method to specify the value to return.
        // Using thenReturn is simple and straightforward.
        // willReturn : This method follows the BDD (Behavior-Driven Development) style.
        // This method is used in combination with given from BDDMockito.
        // Using willReturn makes tests more readable in BDD scenarios.
        // thenReturn and willReturn are both functionally identical, only changing the coding style.
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee))
                .willReturn(employee);

        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();

    }

}
